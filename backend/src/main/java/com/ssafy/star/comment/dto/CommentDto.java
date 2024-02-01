package com.ssafy.star.comment.dto;

import com.ssafy.star.comment.domain.CommentEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record CommentDto(
        Long id,
        Long articleId,
        String nickName,
        String content,
        Long parentId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Set<CommentChildrenDto> childrenComments
) {
    public static CommentDto from(CommentEntity entity) {
        return new CommentDto(
            entity.getId(),
            entity.getArticleEntity().getId(),
            entity.getUserEntity().getNickname(),
            entity.getContent(),
            entity.getParentId(),
            entity.getCreatedAt(),
            entity.getModifiedAt(),
            entity.getChildrenComments().stream().map(CommentChildrenDto::from).collect(Collectors.toSet())
        );
    }
}
