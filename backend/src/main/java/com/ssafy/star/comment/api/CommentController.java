package com.ssafy.star.comment.api;

import com.ssafy.star.comment.application.CommentService;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.comment.dto.request.CommentCreateRequest;
import com.ssafy.star.comment.dto.request.CommentModifyRequest;
import com.ssafy.star.comment.dto.response.CommentResponse;
import com.ssafy.star.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/articles/{articleId}/comments")
    public Response<Page<CommentResponse>> search(@PathVariable Long articleId, Pageable pageable) {
        return Response.success(commentService.search(articleId, pageable).map(CommentResponse::fromComment));
    }

    // 댓글 생성
    // commentId가 들어오는 경우 - 대댓글, null인 경우 - 댓글
    @PostMapping("/articles/{articleId}/comments")
    public Response<CommentDto> create(Authentication authentication, @PathVariable Long articleId, @RequestBody CommentCreateRequest request) {
        return Response.success(commentService.create(articleId, authentication.getName(), request.content(), request.parentId()));
    }

    // 댓글 수정
    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public Response<CommentDto> modify(Authentication authentication, @PathVariable Long articleId, @PathVariable Long commentId, @RequestBody CommentModifyRequest request) {
        return Response.success(commentService.modify(commentId, authentication.getName(), request.content()));
    }

    // 댓글 삭제
    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public Response<Void> delete(Authentication authentication, @PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.delete(commentId, authentication.getName());
        return Response.success();
    }
}