package com.ssafy.star.comment.dto;

import com.ssafy.star.article.ArticleEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.user.UserEntity;

import java.time.LocalDateTime;
import java.util.Set;

public record CommentDto(
        Long id,
        Long articleId,
        String userName,
        String content,
        Long parentCommentId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Set<CommentEntity> childrenComments
) {
    public static CommentDto from(CommentEntity entity) {
        return new CommentDto(
            entity.getId(),
            entity.getArticleEntity().getId(),
            entity.getUserEntity().getUserName(),
            entity.getContent(),
            entity.getParentCommentId(),
            entity.getCreatedAt(),
            entity.getModifiedAt(),
            entity.getChildrenComments()
        );
    }

    public static CommentEntity toEntity(UserEntity userEntity, ArticleEntity articleEntity, String content, Long parentCommentId) {
        return CommentEntity.of(userEntity, articleEntity, content, parentCommentId);
    }
}
