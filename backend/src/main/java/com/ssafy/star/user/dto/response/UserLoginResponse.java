package com.ssafy.star.user.dto.response;

public record UserLoginResponse(
        // TODO: 유저 정보 불러오기
        UserResponse user,
        String token
) {
}
