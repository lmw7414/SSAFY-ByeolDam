package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLikeEntity, Long> {
    Optional<ArticleLikeEntity> findByUserEntityAndArticleEntity(UserEntity userEntity, ArticleEntity articleEntity);

    @Query(value = "SELECT COUNT(*) FROM ArticleLikeEntity entity WHERE entity.articleEntity =:articleEntity")
    Integer countByArticleEntity(ArticleEntity articleEntity);
    void deleteAllByArticleEntity(ArticleEntity articleEntity);
    void deleteAllByUserEntity(UserEntity userEntity);

    List<ArticleLikeEntity> findAllByArticleEntity(ArticleEntity articleEntity);
    Page<ArticleLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable);
}
