package com.ssafy.star.article.api;


import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.article.dto.request.ArticleCreateRequest;
import com.ssafy.star.article.dto.request.ArticleModifyRequest;
import com.ssafy.star.article.dto.response.ArticleResponse;
import com.ssafy.star.article.dto.response.Response;
import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.image.ImageType;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;


    @PostMapping
    public Response<Void> create(@RequestPart ArticleCreateRequest request, Authentication authentication, @RequestParam MultipartFile imageFile) throws IOException {
        // TODO : image
        log.info("request 정보 : {}", request);
        if(imageFile != null){
            articleService.create(request.title(), request.tag(), request.description(),
                    request.disclosureType(), authentication.getName(), imageFile, ImageType.ARTICLE);
        }

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
        String email = authentication.getName();
        return Response.success(articleService.list(email, pageable).map(ArticleResponse::fromArticle));
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
    public Response<ArticleResponse> read(@PathVariable Long articleId, Authentication authentication, Pageable pageable) {
        String email = authentication.getName();

        return Response.success(ArticleResponse.fromArticle(articleService.detail(articleId, email)));
    }
}
