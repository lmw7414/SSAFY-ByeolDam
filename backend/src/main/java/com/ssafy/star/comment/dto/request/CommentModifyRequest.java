package com.ssafy.star.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentModifyRequest {
    private Long commentId;
    private String content;
}
