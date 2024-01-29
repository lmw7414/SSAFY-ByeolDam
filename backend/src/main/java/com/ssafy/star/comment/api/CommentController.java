package com.ssafy.star.comment.api;

import com.ssafy.star.comment.application.CommentService;
import com.ssafy.star.comment.dto.request.CommentCreateRequest;
import com.ssafy.star.comment.dto.request.CommentModifyRequest;
import com.ssafy.star.comment.dto.response.CommentResponse;
import com.ssafy.star.comment.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/{articleId}/comments")
    public Response<Page<CommentResponse>> search(@PathVariable Long articleId, Pageable pageable) {
        return Response.success(commentService.search(articleId, pageable).map(CommentResponse::fromComment));
    }

    // 댓글 생성
    // commentId가 들어오는 경우 - 대댓글, null인 경우 - 댓글
    @PostMapping("/articles/{articleId}/comments")
    public Response<Void> create(@RequestBody CommentCreateRequest request, Authentication authentication) {
        commentService.create(request.getArticleId(), authentication.getName(), request.getContent(), request.getCommentId());
        return Response.success();
    }

    // 댓글 수정
    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public Response<Void> modify(@RequestBody CommentModifyRequest request, Authentication authentication) {
        commentService.modify(request.getCommentId(), authentication.getName(), request.getContent());
        return Response.success();
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public Response<Void> delete(@PathVariable Long commentId, Authentication authentication) {
        commentService.delete(commentId, authentication.getName());
        return Response.success();
    }
}