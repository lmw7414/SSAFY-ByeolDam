package com.ssafy.star.user.dto.response;

import com.ssafy.star.user.dto.User;

public record UserResponse(
        //TODO: 사진, 팔로워, 팔로잉 추가
        String name,
        String nickname,
        String memo

) {

    public static UserResponse fromUser(User dto) {
        return new UserResponse(dto.name(), dto.nickname(), dto.memo());
    }
}
