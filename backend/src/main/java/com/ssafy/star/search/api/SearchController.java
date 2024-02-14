package com.ssafy.star.search.api;

import com.ssafy.star.article.dto.Article;
import com.ssafy.star.article.dto.response.ArticleResponse;
import com.ssafy.star.article.dto.response.Response;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.contour.domain.ContourEntity;
import com.ssafy.star.contour.dto.Contour;
import com.ssafy.star.contour.dto.ContourResponse;
import com.ssafy.star.search.application.ArticleSearchService;
import com.ssafy.star.search.application.ConstellationSearchService;
import com.ssafy.star.search.application.UserSearchService;
import com.ssafy.star.search.dto.response.ConstellationSearchResponse;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.dto.response.SearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {

    private final ArticleSearchService articleSearchService;
    private final ConstellationSearchService constellationSearchService;
    private final UserSearchService userSearchService;

    @Operation(
            summary = "제목 검색 기능",
            description = "제목 검색 기능입니다. " +
                    "게시물의 제목을 기준으로 게시물을 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/search/title")
    public Response<List<ArticleResponse>> titleSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(articleSearchService.titleSearch(keyword).stream().map(Article::fromEntity).map(ArticleResponse::fromArticle).toList());
    }

    @Operation(
            summary = "해시태그 검색 기능",
            description = "해시태그 검색 기능입니다. " +
                    "게시물의 해시태그를 기준으로 게시물을 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/search/hashtag")
    public Response<List<ArticleResponse>> hashtagSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(articleSearchService.hashtagSearch(keyword).stream().map(Article::fromEntity).map(ArticleResponse::fromArticle).toList());
    }

    @Operation(
            summary = "별자리 검색 기능",
            description = "별자리 검색 기능입니다. " +
            "별자리 name을 기준으로 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ConstellationSearchResponse.class)))
            }
    )
    @GetMapping("/search/constellation")
    public Response<List<ConstellationSearchResponse>> constellationSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);

        // 별자리 검색 반환 타입은 ConstellationEntity
        List<ConstellationEntity> constellationEntities = constellationSearchService.constellationSearch(keyword);
        List<ConstellationSearchResponse> constellationSearchResponses = new ArrayList<>();

        // 별자리 Entity를 별자리 SearchResponse로 변환하는 과정
        for (ConstellationEntity constellationEntity : constellationEntities) {
            ContourEntity contourEntity = constellationSearchService.findById(constellationEntity.getContourId());
            ContourResponse contourResponse = ContourResponse.fromContour(Contour.fromEntity(contourEntity));
            constellationSearchResponses.add(new ConstellationSearchResponse(
                    constellationEntity.getId(),
                    constellationEntity.getName(),
                    contourResponse,
                    constellationEntity.getHits(),
                    constellationEntity.getAdminEntity().getNickname(),
                    constellationEntity.getArticleEntities().size(),
                    constellationEntity.getCreatedAt(),
                    constellationEntity.getModifiedAt()
            ));
        }

        return Response.success(constellationSearchResponses);
    }

    @Operation(
            summary = "유저 검색 기능",
            description = "유저 검색 기능입니다. " +
                    "닉네임을 기준으로 게시물을 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/search/user")
    public Response<List<SearchResponse>> userSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(userSearchService.userSearch(keyword).stream().map(User::fromEntity).map(SearchResponse::fromUser).toList());
    }
}
