package com.ssafy.star.user.dto.response;

import com.ssafy.star.user.dto.User;

public record UserJoinResponse(
        Long id,
        String email,
        String name,
        String nickname
) {
    public static UserJoinResponse fromUser(User dto) {
        return new UserJoinResponse(
                dto.id(),
                dto.email(),
                dto.name(),
                dto.nickname()
        );
    }
}
