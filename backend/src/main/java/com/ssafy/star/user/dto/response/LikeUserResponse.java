package com.ssafy.star.user.dto.response;

import com.ssafy.star.user.dto.User;

public record LikeUserResponse(
        String nickname,
        String name,
        String image
) {
    public static LikeUserResponse fromUser(User dto) {
        String imageUrl = "";
        if (dto.image().url() != null) {
            imageUrl = dto.image().url();
        }
        return new LikeUserResponse(
        dto.nickname(),
        dto.name(),
        imageUrl
        );
    }
}
