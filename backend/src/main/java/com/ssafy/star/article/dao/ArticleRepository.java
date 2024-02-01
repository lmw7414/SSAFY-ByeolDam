package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.article.DisclosureType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
        @Query("SELECT a FROM ArticleEntity a WHERE a.user = :user OR a.disclosure = 'VISIBLE'")
        Page<ArticleEntity> findArticlesForUser(@Param("user") UserEntity userEntity, Pageable pageable);

        Page<ArticleEntity> findAllByUser(UserEntity entity, Pageable pageable);

        // DisclosureType이 VISIBLE인 ArticleEntity들만 가져오는 메소드 추가
        Page<ArticleEntity> findByDisclosure(DisclosureType disclosure, Pageable pageable);
}

