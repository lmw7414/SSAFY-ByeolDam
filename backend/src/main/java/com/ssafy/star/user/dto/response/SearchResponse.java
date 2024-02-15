package com.ssafy.star.user.dto.response;

import com.ssafy.star.user.dto.User;

public record SearchResponse (
        String imageUrl,
        String email,
        String name,
        String nickname
){
    public static SearchResponse fromUser(User dto){
        return new SearchResponse(
                dto.image() == null ? "https://byeoldam.s3.ap-northeast-2.amazonaws.com/profiles/defaultProfileImage.png" : dto.image().url(),
                dto.email(),
                dto.name(),
                dto.nickname()
        );
    }
}
