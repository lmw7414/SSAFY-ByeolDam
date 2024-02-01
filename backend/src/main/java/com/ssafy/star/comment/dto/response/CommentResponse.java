package com.ssafy.star.comment.dto.response;

import com.ssafy.star.comment.dto.CommentChildrenDto;
import com.ssafy.star.comment.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.Set;

public record CommentResponse (
        Long id,
        String nickName,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long parentId,
        Set<CommentChildrenDto> childrenComments
) {
    public static CommentResponse fromComment(CommentDto commentDto) {
        return new CommentResponse(
                commentDto.id(),
                commentDto.nickName(),
                commentDto.content(),
                commentDto.createdAt(),
                commentDto.modifiedAt(),
                commentDto.parentId(),
                commentDto.childrenComments()
        );
    }
}
