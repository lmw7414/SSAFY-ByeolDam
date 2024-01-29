package com.ssafy.star.comment.application;

import com.ssafy.star.article.ArticleEntity;
import com.ssafy.star.article.ArticleRepository;
import com.ssafy.star.comment.dao.CommentRepository;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.comment.exception.ErrorCode;
import com.ssafy.star.comment.exception.StarApplicationException;
import com.ssafy.star.user.UserEntity;
import com.ssafy.star.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final ArticleRepository articleRepositoty;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<CommentDto> search(Long articleId, Pageable pageable) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        return commentRepository.findAllByArticleEntity(articleEntity, pageable).map(CommentDto::from);
    }

    @Transactional
    public void create(Long articleId, String nickName, String content, Long parentCommentId) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrException(nickName);
        commentRepository.save(CommentDto.toEntity(userEntity, articleEntity, content, parentCommentId));
    }

    @Transactional
    public void modify(Long commentId, String nickName, String content) {
        CommentEntity commentEntity = getCommentEntityOrException(commentId);
        UserEntity userEntity = getUserEntityOrException(nickName);

        // 수정하려는 사람이 댓글을 작성한 사람인지 확인
        if (commentEntity.getUserEntity() != userEntity) {
            throw new StarApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", nickName, commentId));
        }

        // 댓글 수정
        commentEntity.setContent(content);
    }

    @Transactional
    public void delete(Long commentId, String nickName) {
        CommentEntity commentEntity = getCommentEntityOrException(commentId);
        UserEntity userEntity = getUserEntityOrException(nickName);

        // 수정하려는 사람이 댓글을 작성한 사람이거나 게시글 작성자인지 확인
        if (!(commentEntity.getUserEntity() == userEntity || commentEntity.getArticleEntity().getUser() == userEntity)) {
            throw new StarApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", nickName, commentId));
        }

        commentRepository.delete(commentEntity);
    }

    // 포스트가 존재하는지
    private ArticleEntity getArticleEntityOrException(Long articleId) {
        return articleRepositoty.findById(articleId).orElseThrow(() ->
                new StarApplicationException(ErrorCode.ARTICLE_NOT_FOUND, String.format("%s not founded", articleId)));
    }

    // 유저가 존재하는지
    private UserEntity getUserEntityOrException(String nickName) {
        return userRepository.findByNickname(nickName).orElseThrow(() ->
                new StarApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", nickName)));
    }

    // 댓글이 존재하는지
    private CommentEntity getCommentEntityOrException(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new StarApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("%d not founded", commentId)));
    }
}
