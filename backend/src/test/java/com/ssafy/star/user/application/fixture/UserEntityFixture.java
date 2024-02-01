package com.ssafy.star.user.application.fixture;

import com.ssafy.star.user.domain.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String email, String password, String name, String nickname) {
        UserEntity entity = UserEntity.of(email, password, name, nickname);
        return entity;
    }

    public static UserEntity get(String email) {
        UserEntity entity = UserEntity.of(email, null, null, null);
        return entity;
    }
}
