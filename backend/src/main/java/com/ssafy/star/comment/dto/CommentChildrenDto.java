package com.ssafy.star.comment.dto;

import com.ssafy.star.comment.domain.CommentEntity;

import java.time.LocalDateTime;

public record CommentChildrenDto(
        Long id,
        String nickName,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static CommentChildrenDto from(CommentEntity entity) {
        return new CommentChildrenDto(
                entity.getId(),
                entity.getUserEntity().getNickname(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}