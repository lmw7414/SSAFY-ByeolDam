package com.ssafy.star.article.application.fixture;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;

public class ArticleEntityFixture {
    public static ArticleEntity get(String title, String tag, String description, DisclosureType disclosure,
                                    String email, String password, String name, String nickname) {
        UserEntity userEntity = UserEntity.of(email, password, name, nickname);

        ArticleEntity entity = ArticleEntity.of(title, tag, description, disclosure, userEntity);
        return entity;
    }
}