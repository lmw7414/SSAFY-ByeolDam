package com.ssafy.star.fixture;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.UserEntity;

public class ArticleEntityFixture {
    public static ArticleEntity get(Long ArticleId, Long userId, String userName) {
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        user.setUserName(userName);

        ArticleEntity result = new ArticleEntity();
        result.setUser(user);
        result.setId(ArticleId);
        return result;
    }
}