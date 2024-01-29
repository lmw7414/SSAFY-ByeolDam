package com.ssafy.star.like.domain;

import com.ssafy.star.article.ArticleEntity;
import com.ssafy.star.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "articleLike")
@Getter
@Setter
public class ArticleLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "articleId", nullable = false)
    private ArticleEntity articleEntity;

    protected ArticleLikeEntity() {}

    private ArticleLikeEntity(UserEntity userEntity, ArticleEntity articleEntity) {
        this.userEntity = userEntity;
        this.articleEntity = articleEntity;
    }

    public static ArticleLikeEntity of(UserEntity userEntity, ArticleEntity articleEntity) {
        return new ArticleLikeEntity(userEntity, articleEntity);
    }
}
