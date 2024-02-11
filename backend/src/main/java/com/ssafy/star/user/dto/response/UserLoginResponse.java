package com.ssafy.star.user.dto.response;

public record UserLoginResponse(
        UserResponse user,
        String token
) {
}
