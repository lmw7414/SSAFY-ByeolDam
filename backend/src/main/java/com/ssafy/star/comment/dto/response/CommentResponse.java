package com.ssafy.star.comment.dto.response;

import com.ssafy.star.comment.dto.CommentChildrenDto;
import com.ssafy.star.comment.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long parentCommentId;
    private Set<CommentChildrenDto> childrenComments;

    public static CommentResponse fromComment(CommentDto commentDto) {
        return new CommentResponse(
                commentDto.id(),
                commentDto.content(),
                commentDto.createdAt(),
                commentDto.modifiedAt(),
                commentDto.parentCommentId(),
                commentDto.childrenComments()
        );
    }
}
