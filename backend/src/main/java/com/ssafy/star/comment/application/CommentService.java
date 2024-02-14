package com.ssafy.star.comment.application;


import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.comment.repository.CommentRepository;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.notification.dto.NotificationArgs;
import com.ssafy.star.notification.dto.NotificationEvent;
import com.ssafy.star.notification.dto.NotificationType;
import com.ssafy.star.notification.producer.NotificationProducer;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final ArticleRepository articleRepositoty;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;

    // TODO : 예시) articleId 1에서 한 댓글에 parentId를 지정해놓고 articleId 2로 대댓글 작성 요청하면 작성이 됨. 예외 처리할것
    // 댓글 조회
    @Transactional(readOnly = true)
    public Page<CommentDto> search(Long articleId, Pageable pageable) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        return commentRepository.findByArticleEntityAndParentId(articleEntity, pageable).map(CommentDto::from);
    }

    // 댓글 생성
    @Transactional
    public CommentDto create(Long articleId, String email, String content, Long parentId) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrException(email);
        // 대댓글인 경우, 부모댓글이 존재하는지 확인
        if (parentId != null) {
            CommentEntity parentCommentEntity = getCommentEntityOrException(parentId);
            if (parentCommentEntity.getParentId() != null) {
                throw new ByeolDamException(ErrorCode.REPLY_TO_REPLY);
            }
            getCommentEntityOrException(parentId);
        }

        // 내용이 빈 문자열인지 확인
        if (content.isBlank()) {
            throw new ByeolDamException(ErrorCode.INVALID_CONTENT);
        }

        // 댓글 생성
        CommentEntity commentEntity = commentRepository.save(CommentEntity.of(userEntity, articleEntity, content, parentId));

        if (parentId != null) {
            // 대댓글 알림 - 댓글 작성자와 대댓글 작성자가 다른 경우 알람 보내기
            CommentEntity parentCommentEntity = getCommentEntityOrException(parentId);
            if (!parentCommentEntity.getUserEntity().equals(userEntity)) {
                notificationProducer.send(new NotificationEvent(NotificationType.NEW_COMMENT_ON_COMMENT, new NotificationArgs(userEntity.getId(), parentId), parentCommentEntity.getUserEntity().getId()));
            }
        } else {
            // 댓글 알림 - 게시글 작성자와 댓글 작성자가 다른 경우 알람 보내기
            if (!articleEntity.getOwnerEntity().equals(userEntity)) {
                notificationProducer.send(new NotificationEvent(NotificationType.NEW_COMMENT_ON_POST, new NotificationArgs(userEntity.getId(), articleId), articleEntity.getOwnerEntity().getId()));
            }
        }
        return CommentDto.from(commentEntity);
    }

    // 댓글 수정
    @Transactional
    public CommentDto modify(Long commentId, String email, String content) {
        CommentEntity commentEntity = getCommentEntityOrException(commentId);
        UserEntity userEntity = getUserEntityOrException(email);

        // 수정하려는 사람이 댓글을 작성한 사람인지 확인
        if (commentEntity.getUserEntity() != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", email, commentId));
        }
        // 내용이 빈 문자열인지 확인
        if (content.isBlank()) {
            throw new ByeolDamException(ErrorCode.INVALID_CONTENT);
        }

        commentEntity.setContent(content);

        return CommentDto.from(commentRepository.saveAndFlush(commentEntity));
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long commentId, String email) {
        log.info("commentID: {} email: {}", commentId, email);
        CommentEntity commentEntity = getCommentEntityOrException(commentId);
        UserEntity userEntity = getUserEntityOrException(email);

        // 수정하려는 사람이 댓글을 작성한 사람이거나 게시글 작성자인지 확인
        if (!(commentEntity.getUserEntity() == userEntity || commentEntity.getArticleEntity().getOwnerEntity() == userEntity)) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", email, commentId));
        }

        commentRepository.delete(commentEntity);
    }

    // 포스트가 존재하는지
    private ArticleEntity getArticleEntityOrException(Long articleId) {
        return articleRepositoty.findById(articleId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND, String.format("%s not founded", articleId)));
    }

    // 유저가 존재하는지
    private UserEntity getUserEntityOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));
    }

    // 댓글이 존재하는지
    private CommentEntity getCommentEntityOrException(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.COMMENT_NOT_FOUND, String.format("%d not founded", commentId)));
    }
}
