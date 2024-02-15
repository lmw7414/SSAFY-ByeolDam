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
                dto.image() == null ? "https://byeoldam.s3.ap-northeast-2.amazonaws.com/profiles/defaultProfileImage.png" : dto.image().url()
        );
    }
}
