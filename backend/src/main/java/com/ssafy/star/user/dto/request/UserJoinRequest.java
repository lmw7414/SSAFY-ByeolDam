package com.ssafy.star.user.dto.request;

public record UserJoinRequest(
        String email,
        String password,
        String name,
        String nickname
) {
}
