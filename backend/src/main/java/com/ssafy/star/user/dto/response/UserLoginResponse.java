package com.ssafy.star.user.dto.response;

public record UserLoginResponse(
        UserDefaultResponse user,
        String token
) {
}
