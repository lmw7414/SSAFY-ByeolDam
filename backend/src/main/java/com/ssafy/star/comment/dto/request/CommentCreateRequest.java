package com.ssafy.star.comment.dto.request;

public record CommentCreateRequest(
        String content,
        Long parentId
) {
}
