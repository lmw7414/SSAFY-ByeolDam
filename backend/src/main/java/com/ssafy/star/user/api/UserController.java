package com.ssafy.star.user.api;

import com.ssafy.star.common.response.Response;
import com.ssafy.star.user.application.UserService;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.dto.request.UserJoinRequest;
import com.ssafy.star.user.dto.request.UserLoginRequest;
import com.ssafy.star.user.dto.request.UserModifyRequest;
import com.ssafy.star.user.dto.response.UserJoinResponse;
import com.ssafy.star.user.dto.response.UserLoginResponse;
import com.ssafy.star.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.email(), request.password(), request.name(), request.nickname());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.email(), request.password());
        return Response.success(new UserLoginResponse(token));
    }

    //중복된 이메일 확인 체크(참일 경우 중복 없음)
    @PostMapping("/check-email")
    public Response<Boolean> checkDuplicateEmail(@RequestBody Map<String, String> emailMap) {
        return Response.success(userService.checkDuplicateEmail(emailMap.get("email")));
    }

    //중복된 닉네임 확인 체크(참일 경우 중복 없음)
    @PostMapping("/check-nickname")
    public Response<Boolean> checkDuplicateNickname(@RequestBody Map<String, String> nicknameMap) {
        return Response.success(userService.checkDuplicateNickname(nicknameMap.get("nickname")));
    }

    @GetMapping("/{nickname}")
    public Response<UserResponse> myProfile(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(UserResponse.fromUser(userService.my(nickname)));
    }

    @PutMapping
    public Response<Void> updateMyProfile(Authentication authentication, @RequestBody UserModifyRequest request) {
        userService.updateMyProfile(request.password(), request.name(), request.nickname(), request.memo(), request.disclosureType(), request.birthday());
        return Response.success();
    }

    @DeleteMapping
    public Response<Void> deleteUser(Authentication authentication) {
        userService.delete(authentication.getName());
        return Response.success();
    }
}
