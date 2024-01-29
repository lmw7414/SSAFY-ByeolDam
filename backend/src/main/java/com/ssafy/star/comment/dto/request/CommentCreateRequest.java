package com.ssafy.star.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateRequest {
    private Long articleId;
    private String content;
    private Long commentId;
}
