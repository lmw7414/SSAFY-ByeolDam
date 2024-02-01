package com.ssafy.star.user.api;

import com.ssafy.star.common.response.Response;
import com.ssafy.star.user.application.FollowService;
import com.ssafy.star.user.application.UserService;
import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.dto.request.FollowAcceptRequest;
import com.ssafy.star.user.dto.request.UserJoinRequest;
import com.ssafy.star.user.dto.request.UserLoginRequest;
import com.ssafy.star.user.dto.request.UserModifyRequest;
import com.ssafy.star.user.dto.response.UserJoinResponse;
import com.ssafy.star.user.dto.response.UserLoginResponse;
import com.ssafy.star.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    @PostMapping("/users/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.email(), request.password(), request.name(), request.nickname());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/users/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.email(), request.password());
        return Response.success(new UserLoginResponse(token));
    }

    //중복된 이메일 확인 체크(참일 경우 중복 없음)
    @PostMapping("/users/check-email")
    public Response<Boolean> checkDuplicateEmail(@RequestBody Map<String, String> emailMap) {
        return Response.success(userService.checkDuplicateEmail(emailMap.get("email")));
    }

    //중복된 닉네임 확인 체크(참일 경우 중복 없음)
    @PostMapping("/users/check-nickname")
    public Response<Boolean> checkDuplicateNickname(@RequestBody Map<String, String> nicknameMap) {
        return Response.success(userService.checkDuplicateNickname(nicknameMap.get("nickname")));
    }

    @GetMapping("/users/{nickname}")
    public Response<UserResponse> myProfile(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(UserResponse.fromUser(userService.my(nickname)));
    }

    @PutMapping("/users")
    public Response<Void> updateMyProfile(Authentication authentication, @RequestBody UserModifyRequest request) {
        userService.updateMyProfile(authentication.getName(), request.password(), request.name(), request.nickname(), request.memo(), request.disclosureType(), request.birthday());
        return Response.success();
    }

    @DeleteMapping("/users")
    public Response<Void> deleteUser(Authentication authentication) {
        userService.delete(authentication.getName());
        return Response.success();
    }

    // 팔로우 요청하기
    @PostMapping("/{nickname}/follow")
    public Response<ApprovalStatus> requestFollow(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.requestFollow(authentication.getName(), nickname));
    }
    // 팔로우 상태 확인하기
    @GetMapping("/{nickname}/follow")
    public Response<ApprovalStatus> followStatus(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.followStatus(authentication.getName(), nickname));
    }


    // 나에게 팔로우를 신청한 사람들의 리스트 보기
    @GetMapping("me/request-follow")
    public Response<List<UserResponse>> requestFollowList(Authentication authentication) {
        return Response.success(followService.requestFollowList(authentication.getName())
                .stream()
                .map(UserResponse::fromUser)
                .toList());
    }
    // 팔로우 요청 승인하기
    @PostMapping("/me/approve-follow")
    public Response<ApprovalStatus> acceptFollow(Authentication authentication, @RequestBody FollowAcceptRequest request) {
        return Response.success(followService.acceptFollow(authentication.getName(), request.toUserNickname(), request.status()));
    }


    // 내 팔로워 확인하기
    @GetMapping("/me/followers")
    public Response<List<UserResponse>> followers(Authentication authentication) {
        return Response.success(followService.followers(authentication.getName()).stream().map(UserResponse::fromUser).toList());
    }
    // 내 팔로잉 확인하기
    @GetMapping("/me/followings")
    public Response<List<UserResponse>> followings(Authentication authentication) {
        return Response.success(followService.followings(authentication.getName()).stream().map(UserResponse::fromUser).toList());
    }
    //남의 팔로워 확인하기
    @GetMapping("/{nickname}/followers")
    public Response<List<UserResponse>> otherFollowers(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.otherFollowers(authentication.getName(), nickname).stream().map(UserResponse::fromUser).toList());

    }
    // 남의 팔로잉 확인하기
    @GetMapping("/{nickname}/followings")
    public Response<List<UserResponse>> otherFollowings(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.otherFollowings(authentication.getName(), nickname).stream().map(UserResponse::fromUser).toList());
    }

    // 팔로워 수 반환하기
    @GetMapping("/{nickname}/count-followers")
    public Response<Long> countFollowers(@PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.countFollowers(nickname));
    }
    // 팔로잉 수 반환하기
    @GetMapping("/{nickname}/count-followings")
    public Response<Long> countFollowings(@PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.countFollowings(nickname));
    }
}
