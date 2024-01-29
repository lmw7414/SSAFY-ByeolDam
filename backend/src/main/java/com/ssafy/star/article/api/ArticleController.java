package com.ssafy.star.article.api;


import com.ssafy.star.article.api.request.ArticleCreateRequest;
import com.ssafy.star.article.api.request.ArticleModifyRequest;
import com.ssafy.star.article.api.response.ArticleResponse;
import com.ssafy.star.article.api.response.Response;
import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.dto.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public Response<Void> create(@RequestBody ArticleCreateRequest request, Authentication authentication) {
        // TODO : image
        articleService.create(request.getTitle(), request.getTag(), request.getDescription(),
                request.getDisclosureType(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{articleId}")
    public Response<ArticleResponse> modify(@PathVariable Long articleId, @RequestBody ArticleModifyRequest request, Authentication authentication) {
        // TODO : image
        Article article = articleService.modify(articleId, request.getTitle(), request.getTag(),
                request.getDescription(), request.getDisclosureType(), authentication.getName());
        return Response.success(ArticleResponse.fromArticle(article));
    }

    @DeleteMapping("/{articleId}")
    public Response<Void> delete(@PathVariable Long articleId, Authentication authentication) {
        articleService.delete(articleId, authentication.getName());
        return Response.success();
    }

    /**
     * 게시글 전체 조회
     * : 검색 시 전체 조회 필요할듯
     */
    @GetMapping
    public Response<Page<ArticleResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(articleService.list(pageable).map(ArticleResponse::fromArticle));
    }

    /**
     * 내 게시물 전체 조회
     */
    @GetMapping("/my")
    public Response<Page<ArticleResponse>> my(Pageable pageable, Authentication authentication) {
        return Response.success(articleService.my(authentication.getName(), pageable).map(ArticleResponse::fromArticle));
    }

    /**
     * 게시물 상세 조회
     */
    @GetMapping("/{id}")
    public Response<ArticleResponse> read(@PathVariable Long articleId, Authentication authentication) {
        return Response.success(ArticleResponse.fromArticle(articleService.detail(articleId)));
    }
}
