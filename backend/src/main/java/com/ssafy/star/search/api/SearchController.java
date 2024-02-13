package com.ssafy.star.search.api;

import com.ssafy.star.article.dto.Article;
import com.ssafy.star.article.dto.response.ArticleResponse;
import com.ssafy.star.article.dto.response.Response;
import com.ssafy.star.search.application.ArticleSearchService;
import com.ssafy.star.search.application.UserSearchService;
import com.ssafy.star.search.dto.request.SearchRequest;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.dto.response.SearchResponse;
import com.ssafy.star.user.dto.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {

    private final ArticleSearchService articleSearchService;
    private final UserSearchService userSearchService;

    @Operation(
            summary = "제목 검색 기능",
            description = "제목 검색 기능입니다. " +
                "게시물의 제목을 기준으로 게시물을 찾습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/search/title")
    public Response<Page<ArticleResponse>> titleSearch(@RequestBody SearchRequest request, Pageable pageable) {
        log.info("request 정보 : {}", request);
        String keyword = request.keyword();
        return Response.success(articleSearchService.titleSearch(keyword,pageable).map(Article::fromEntity).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "해시태그 검색 기능",
            description = "해시태그 검색 기능입니다. " +
                    "게시물의 해시태그를 기준으로 게시물을 찾습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/search/hashtag")
    public Response<Page<ArticleResponse>> hashtagSearch(@RequestBody SearchRequest request, Pageable pageable) {
        log.info("request 정보 : {}", request);
        String keyword = request.keyword();
        return Response.success(articleSearchService.hashtagSearch(keyword,pageable).map(Article::fromEntity).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "유저 검색 기능",
            description = "유저 검색 기능입니다. " +
                    "유저를 기준으로 게시물을 찾습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/search/user")
    public Response<Page<SearchResponse>> userSearch(@RequestBody SearchRequest request, Pageable pageable) {
        log.info("request 정보 : {}", request);
        String keyword = request.keyword();
        return Response.success(userSearchService.userSearch(keyword,pageable).map(User::fromEntity).map(SearchResponse::fromUser));
    }

}
