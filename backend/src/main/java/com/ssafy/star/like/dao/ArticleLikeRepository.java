package com.ssafy.star.like.dao;

import com.ssafy.star.article.ArticleEntity;
import com.ssafy.star.like.domain.ArticleLikeEntity;
import com.ssafy.star.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLikeEntity, Long> {
    Optional<ArticleLikeEntity> findByUserAndArticle(UserEntity userEntity, ArticleEntity articleEntity);

    @Query(value = "SELECT COUNT(*) FROM ArticleLikeEntity entity WHERE entity.articleEntity =:articleEntity")
    Integer countByArticle(@Param("article") ArticleEntity articleEntity);

    List<ArticleLikeEntity> findAllByArticle(ArticleEntity articleEntity);
}
