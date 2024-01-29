package com.ssafy.star.comment.fixture;

import com.ssafy.star.article.ArticleEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.user.UserEntity;

public class CommentEntityFixture {
    public static CommentEntity get(Long id, String content, UserEntity userEntity, ArticleEntity articleEntity) {
        CommentEntity commentEntity = CommentEntity.of(null, null, null, null);
        commentEntity.setId(id);
        commentEntity.setContent(content);
        commentEntity.setUserEntity(userEntity);
        commentEntity.setArticleEntity(articleEntity);
        return commentEntity;
    }
}
