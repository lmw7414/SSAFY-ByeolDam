package com.ssafy.star.user.dto.response;

import com.ssafy.star.user.dto.User;

// 팔로워 팔로우 관련 Response
public record FollowResponse(
        String imageUrl,
        String email,
        String name,
        String nickname,
        Integer articleCounts,
        Integer constellationCounts
) {
    public static FollowResponse fromFollow(User dto,int articleCounts, int constellationCounts){
        return new FollowResponse(
                dto.image() == null ? "" : dto.image().url(),
                dto.email(),
                dto.name(),
                dto.nickname(),
                articleCounts,
                constellationCounts
        );
    }
}
