package com.ssafy.star.user.dto.response;

import com.ssafy.star.user.dto.User;

public record LikeUserResponse(
        String nickname,
        String name,
        String image
) {
    public static LikeUserResponse fromUser(User dto) {
        return new LikeUserResponse(
        dto.nickname(),
        dto.name(),
        dto.image().url()
        );
    }
}
