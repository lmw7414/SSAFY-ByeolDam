package com.ssafy.star.user.dto.request;

public record UserLoginRequest(
        String email,
        String password
) {
}
