package com.ssafy.star.article.api;


import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.article.dto.request.ArticleCreateRequest;
import com.ssafy.star.article.dto.request.ArticleModifyRequest;
import com.ssafy.star.article.dto.response.ArticleResponse;
import com.ssafy.star.article.dto.response.Response;
import com.ssafy.star.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;


    @PostMapping
    public Response<Void> create(@RequestBody ArticleCreateRequest request, Authentication authentication) {
        // TODO : image
        log.info("request 정보 : {}", request);
        articleService.create(request.title(), request.tag(), request.description(),
                request.disclosureType(), authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "게시물 수정",
            description = "게시물 수정입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 수정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PutMapping("/{articleId}")
    public Response<ArticleResponse> modify(@PathVariable Long articleId, @RequestBody ArticleModifyRequest request, Authentication authentication) {
        // TODO : image
        Article article = articleService.modify(articleId, request.title(), request.tag(), request.description(),
                request.disclosureType(), authentication.getName());
        return Response.success(ArticleResponse.fromArticle(article));
    }

    @DeleteMapping("/{articleId}")
    public Response<Void> delete(@PathVariable Long articleId, Authentication authentication) {
        articleService.delete(articleId, authentication.getName());
        return Response.success();
    }

    // TODO: 검색 시 전체 조회 필요할듯
    @Operation(
            summary = "게시물 전체 조회",
            description = "게시물 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping
    public Response<Page<ArticleResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(articleService.list(pageable).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "내 게시물 전체 조회",
            description = "내 게시물 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/my")
    public Response<Page<ArticleResponse>> my(Pageable pageable, Authentication authentication) {
        return Response.success(articleService.my(authentication.getName(), pageable).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "게시물 상세 조회",
            description = "게시물 상세 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/{articleId}")
    public Response<ArticleResponse> read(@PathVariable Long articleId, Authentication authentication) {
        return Response.success(ArticleResponse.fromArticle(articleService.detail(articleId)));
    }

    @Operation(
            summary = "게시물 좋아요 요청",
            description = "게시물 좋아요를 요청하고 게시물 작성자에게 알람을 보냅니다."
    )
    @PostMapping("/{articleId}/likes")
    public Response<Void> like(Authentication authentication, @PathVariable Long articleId) {
        articleService.like(articleId, authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "게시물 좋아요 상태 확인",
            description = "게시물 좋아요 상태를 확인합니다."
    )
    @GetMapping("/{articleId}/likes")
    public Response<Boolean> checkLike(Authentication authentication, @PathVariable Long articleId) {
        return Response.success(articleService.checkLike(articleId, authentication.getName()));
    }

    @Operation(
            summary = "게시물 좋아요 갯수 확인",
            description = "게시물 좋아요의 개수를 확인합니다."
    )
    @GetMapping("/{articleId}/likeCount")
    public Response<Integer> likeCount(@PathVariable Long articleId) {
        return Response.success(articleService.likeCount(articleId));
    }

    @Operation(
            summary = "게시물을 좋아요한 사람의 목록 확인",
            description = "게시물을 좋아요한 사람의 목록을 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserResponse.class)))
            }

    )
    @GetMapping("/{articleId}/likeList")
    public Response<List<UserResponse>> likeList(@PathVariable Long articleId) {
        return Response.success(articleService.likeList(articleId).stream().map(UserResponse::fromUser).toList());
    }


}
