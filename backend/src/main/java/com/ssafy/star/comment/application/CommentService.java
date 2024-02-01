package com.ssafy.star.comment.application;


import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.comment.repository.CommentRepository;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
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

    @Transactional(readOnly = true)
    public Page<CommentDto> search(Long articleId, Pageable pageable) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        return commentRepository.findByArticleEntityAndParentId(articleEntity, pageable).map(CommentDto::from);
    }

    @Transactional
    public CommentDto create(Long articleId, String email, String content, Long parentId) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrException(email);

        if (parentId != null) {
            getCommentEntityOrException(parentId);
        }
        if (content.isBlank()) {
            throw new ByeolDamException(ErrorCode.INVALID_CONTENT);
        }

        return CommentDto.from(commentRepository.save(CommentEntity.of(userEntity, articleEntity, content, parentId)));
    }

    @Transactional
    public CommentDto modify(Long commentId, String email, String content) {
        CommentEntity commentEntity = getCommentEntityOrException(commentId);
        UserEntity userEntity = getUserEntityOrException(email);

        // 수정하려는 사람이 댓글을 작성한 사람인지 확인
        if (commentEntity.getUserEntity() != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", email, commentId));
        }
        if (content.isBlank()) {
            throw new ByeolDamException(ErrorCode.INVALID_CONTENT);
        }

        // 댓글 수정
        commentEntity.setContent(content);

        return CommentDto.from(commentRepository.saveAndFlush(commentEntity));
    }

    @Transactional
    public void delete(Long commentId, String email) {
        log.info("commentID: {} email: {}", commentId, email);
        CommentEntity commentEntity = getCommentEntityOrException(commentId);
        UserEntity userEntity = getUserEntityOrException(email);

        // 수정하려는 사람이 댓글을 작성한 사람이거나 게시글 작성자인지 확인
        if (!(commentEntity.getUserEntity() == userEntity || commentEntity.getArticleEntity().getUser() == userEntity)) {
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
