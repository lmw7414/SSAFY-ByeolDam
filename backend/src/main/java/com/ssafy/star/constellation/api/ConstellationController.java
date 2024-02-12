package com.ssafy.star.constellation.api;

import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.common.response.Response;
import com.ssafy.star.constellation.application.ConstellationService;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.constellation.dto.request.ConstellationCreateRequest;
import com.ssafy.star.constellation.dto.request.ConstellationModifyRequest;
import com.ssafy.star.constellation.dto.request.UserEmailRequest;
import com.ssafy.star.constellation.dto.response.ConstellationResponse;
import com.ssafy.star.constellation.dto.response.ConstellationWithArticleResponse;
import com.ssafy.star.user.application.FollowService;
import com.ssafy.star.user.dto.request.NicknameRequest;
import com.ssafy.star.user.dto.response.UserDefaultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConstellationController {
    private final ConstellationService constellationService;
    private final ArticleService articleService;
    private final FollowService followService;

    @Operation(
            summary = "별자리 생성",
            description = "별자리 생성입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PostMapping("/constellations")
    public Response<Void> create(@RequestPart("request") ConstellationCreateRequest request, Authentication authentication,
                                 @RequestPart("origin") MultipartFile origin,
                                 @RequestPart("thumb") MultipartFile thumb,
                                 @RequestPart("cthumb") MultipartFile cthumb,
                                 @RequestPart("contoursList") List<List<List<Integer>>> contoursList,
                                 @RequestPart("ultimate") List<List<Integer>> ultimate
    ) throws IOException {
        //TODO : 윤곽선은 request에 같이 담아져서 옴, request에서 어떻게 추출해서 MongoDB에 저장할지 고민 필요

        // 사용자를 관리자로 만듦
        constellationService.create(
                request.name(),
                request.description(),
                authentication.getName(),
                origin,
                thumb,
                cthumb,
                contoursList,
                ultimate
        );
        return Response.success();
    }

    @Operation(
            summary = "별자리 수정",
            description = "별자리 수정입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PutMapping("/constellations/{constellationId}")
    public Response<ConstellationResponse> modify(
            Authentication authentication,
            @PathVariable Long constellationId,
            @RequestPart("request") ConstellationModifyRequest request,
            @RequestPart("origin") MultipartFile origin,
            @RequestPart("thumb") MultipartFile thumb,
            @RequestPart("cthumb") MultipartFile cthumb,
            @RequestPart("contoursList") List<List<List<Integer>>> contoursList,
            @RequestPart("ultimate") List<List<Integer>> ultimate
    ) throws IOException {
        Constellation constellation = constellationService.modify(
                constellationId,
                request.name(),
                request.description(),
                authentication.getName(),
                origin,
                thumb,
                cthumb,
                contoursList,
                ultimate
        );
        return Response.success(ConstellationResponse.fromConstellation(constellation));
    }

    @Operation(
            summary = "별자리 삭제",
            description = "별자리 삭제입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @DeleteMapping("/constellations/{constellationId}")
    public Response<Void> deleteConstellationWithContour(Authentication authentication, @PathVariable Long constellationId) {
        constellationService.deleteConstellationWithContour(authentication.getName(), constellationId);
        return Response.success();
    }

    @Operation(
            summary = "나의 우주 보기 - 별자리 전체 조회",
            description = "별자리 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @GetMapping("/constellations")
    public Response<List<ConstellationWithArticleResponse>> myConstellations(Authentication authentication) {
        return Response.success(constellationService.myConstellations(authentication.getName()).stream().map(ConstellationWithArticleResponse::fromConstellationWithArticle).toList());
    }

    @Operation(
            summary = "유저의 별자리 전체 조회",
            description = "유저의 별자리 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @GetMapping("/constellations/user/{nickname}")
    public Response<List<ConstellationWithArticleResponse>> userConstellations(Authentication authentication, @PathVariable String nickname) {
        return Response.success(
                constellationService.userConstellations(nickname, authentication.getName())
                        .stream()
                        .map(ConstellationWithArticleResponse::fromConstellationWithArticle)
                        .toList()
        );
    }


    // TODO : 별자리 공유 신청, 수락 로직으로 바꿀 것
    @Operation(
            summary = "공유 별자리에 유저 추가",
            description = "공유 별자리에 유저를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PostMapping("/add-user/constellations/{constellationId}")
    public Response<Void> addUser(Authentication authentication, @PathVariable Long constellationId, @RequestBody NicknameRequest nicknameRequest) {
        String userEmail = nicknameRequest.nickname();
        constellationService.addUser(constellationId, userEmail, authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "공유 별자리의 유저 삭제",
            description = "공유 별자리의 유저를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @DeleteMapping("/delete-user/constellations/{constellationId}")
    public Response<Void> deleteUser(Authentication authentication, @PathVariable Long constellationId, @RequestBody NicknameRequest nicknameRequest) {
        String userEmail = nicknameRequest.nickname();
        constellationService.deleteUser(constellationId, userEmail, authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "공유 별자리의 유저 조회",
            description = "공유 별자리의 유저를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @GetMapping("/users/constellations/{constellationId}")
    public Response<List<UserDefaultResponse>> userCheck(@PathVariable Long constellationId, Authentication authentication, Pageable pageable) {
        return Response.success(constellationService.findConstellationUsers(constellationId, authentication.getName())
                .stream()
                .map(res -> UserDefaultResponse.fromUser(
                                res,
                                articleService.countArticles(res.email()),
                                constellationService.countConstellations(res.email()),
                                followService.countFollowers(res.nickname()),
                                followService.countFollowings(res.nickname())
                        )
                )
                .toList()
        );
    }

    @Operation(
            summary = "공유 별자리의 관리자 변경",
            description = "공유 별자리의 관리자를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PutMapping("/role-modify/constellations/{constellationId}")
    public Response<Void> roleModify(@PathVariable Long constellationId, @RequestBody UserEmailRequest userEmailRequest, Authentication authentication) {
        String userEmail = userEmailRequest.userEmail();
        constellationService.roleModify(constellationId, userEmail, authentication.getName());
        return Response.success();
    }
}
