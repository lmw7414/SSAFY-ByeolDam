package com.ssafy.star.comment.api;

import com.ssafy.star.comment.application.CommentService;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.comment.dto.request.CommentCreateRequest;
import com.ssafy.star.comment.dto.request.CommentModifyRequest;
import com.ssafy.star.comment.dto.response.CommentResponse;
import com.ssafy.star.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comments", description = "댓글 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 조회",
            description = "댓글 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(schema = @Schema(implementation = CommentResponse.class)))
            }
    )
    @GetMapping("/articles/{articleId}/comments")
    public Response<Page<CommentResponse>> search(@PathVariable Long articleId, Pageable pageable) {
        return Response.success(commentService.search(articleId, pageable).map(CommentResponse::fromComment));
    }

    // commentId가 들어오는 경우 - 대댓글, null인 경우 - 댓글
    @Operation(
            summary = "댓글 생성",
            description = "댓글 생성입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 생성 성공", content = @Content(schema = @Schema(implementation = CommentDto.class)))
            }
    )
    @PostMapping("/articles/{articleId}/comments")
    public Response<CommentDto> create(Authentication authentication, @PathVariable Long articleId, @RequestBody CommentCreateRequest request) {
        return Response.success(commentService.create(articleId, authentication.getName(), request.content(), request.parentId()));
    }

    @Operation(
            summary = "댓글 수정",
            description = "댓글 수정입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentDto.class)))
            }
    )
    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public Response<CommentDto> modify(Authentication authentication, @PathVariable Long articleId, @PathVariable Long commentId, @RequestBody CommentModifyRequest request) {
        return Response.success(commentService.modify(commentId, authentication.getName(), request.content()));
    }

    @Operation(
            summary = "댓글 삭제",
            description = "댓글 삭제입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
            }
    )
    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public Response<Void> delete(Authentication authentication, @PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.delete(commentId, authentication.getName());
        return Response.success();
    }
}