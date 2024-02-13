package com.ssafy.star.article.api;


import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.dto.request.ArticleConstellationSelect;
import com.ssafy.star.article.dto.request.ArticleCreateRequest;
import com.ssafy.star.article.dto.request.ArticleDeletionUndo;
import com.ssafy.star.article.dto.request.ArticleModifyRequest;
import com.ssafy.star.article.dto.response.ArticleResponse;
import com.ssafy.star.article.dto.response.Response;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @Operation(
            summary = "게시물 작성",
            description = "게시물 작성입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PostMapping("/articles")
    public Response<Void> create(@RequestPart ArticleCreateRequest request, Authentication authentication, @RequestParam MultipartFile imageFile) {
        log.info("request 정보 : {}", request);
        if (imageFile != null) {
            articleService.create(
                    request.title(),
                    request.description(),
                    request.disclosureType(),
                    authentication.getName(),
                    imageFile,
                    request.imageType(),
                    request.articleHashtagSet()
            );
            return Response.success();
        } else {
            throw new ByeolDamException(ErrorCode.ARTICLE_IMAGE_EMPTY, "article imagefile is empty");
        }
    }

    @Operation(
            summary = "게시물 수정",
            description = "게시물 수정입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 수정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PutMapping("/articles/{articleId}")
    public Response<Void> modify(@PathVariable Long articleId, @RequestBody ArticleModifyRequest request, Authentication authentication) {
        articleService.modify(articleId, request.title(), request.description(),
                request.disclosureType(), authentication.getName(), request.articleHashtagSet());
        return Response.success();
    }

    @Operation(
            summary = "게시물 삭제",
            description = "게시물 삭제입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @DeleteMapping("/articles/{articleId}")
    public Response<Void> delete(@PathVariable Long articleId, Authentication authentication) {
        articleService.delete(articleId, authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "팔로우 피드",
            description = "팔로워들의 게시물을 최신순으로 정렬하여 페이지로 반환합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/follow")
    public Response<Page<ArticleResponse>> followFeed(Authentication authentication, Pageable pageable) {
        String email = authentication.getName();
        return Response.success(articleService.followFeed(email, pageable).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "유저의 게시물 전체 조회",
            description = "유저의 게시물 전체 조회입니다. 유저가 접속자일 경우 전체 조회, " +
                    "접속자가 유저를 팔로우 중일 경우 전체 조회, " +
                    "그 외 discloseType이 VISIBLE인 게시물만 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/user/{nickname}")
    public Response<Page<ArticleResponse>> userArticlePage(@PathVariable String nickname, Authentication authentication,Pageable pageable) {
        List<ArticleResponse> articleResponses = articleService.userArticleList(nickname, authentication.getName()).stream().map(ArticleResponse::fromArticle).toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), articleResponses.size());
        return Response.success(new PageImpl<>(articleResponses.subList(start, end), pageable, articleResponses.size()));
    }

    @Operation(
            summary = "게시물 상세 조회",
            description = "게시물 상세 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/{articleId}")
    public Response<ArticleResponse> read(@PathVariable Long articleId, Authentication authentication, Pageable pageable) {
        String email = authentication.getName();

        return Response.success(ArticleResponse.fromArticle(articleService.detail(articleId, email)));
    }

    @Operation(
            summary = "별자리에 게시물 배정",
            description = "별자리에 게시물을 한개 또는 여러개를 배정합니다. " +
                          "다른 별자리에 있던 게시물을 현 별자리로 옮길 수도 있습니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "배정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PostMapping("/articles/constellation-select/{constellationId}")
    public Response<Void> select(@PathVariable Long constellationId, @RequestBody ArticleConstellationSelect articleConstellationSelect, Authentication authentication) {
        articleService.select(constellationId, articleConstellationSelect.articleIdSet(), authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "별자리에 있는 게시물 전체 조회",
            description = "별자리에 있는 게시물 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/constellation/{constellationId}")
    public Response<List<ArticleResponse>> articlesInConstellation(@PathVariable Long constellationId, Authentication authentication) {
        return Response.success(articleService.articlesInConstellation(constellationId, authentication.getName()).stream().map(ArticleResponse::fromArticle).toList());
    }

    @Operation(
            summary = "휴지통 조회",
            description = "휴지통을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/trashcan")
    public Response<Page<ArticleResponse>> trashcan(Authentication authentication, Pageable pageable) {
        return Response.success(articleService.trashcan(authentication.getName(), pageable).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "게시물 복원",
            description = "휴지통에 있던 게시물을 복원합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "복원 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PutMapping("/articles/trashcan/undo")
    public Response<ArticleResponse> undoDeletion(@RequestBody ArticleDeletionUndo articleDeletionUndo, Authentication authentication) {
        return Response.success(ArticleResponse.fromArticle(articleService.undoDeletion(articleDeletionUndo.articleId(), authentication.getName())));
    }

}
