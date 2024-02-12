package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
        // 게시물 상세 조회(지워지지 않은)
        @Query("SELECT COUNT(a) > 0 FROM ArticleEntity a WHERE a.id = :articleId AND a.deletedAt IS NULL AND (a.ownerEntity = :ownerEntity OR a.disclosure = 'VISIBLE')")
        boolean findByArticleIdAndNotDeleted(@Param("articleId") Long articleId, @Param("ownerEntity") UserEntity ownerEntity);

        /**
         * 유저 게시물 전체 조회(지워지지 않은)
          */
        // userEntity, deletedAt == Null, VISIBLE
        @Query("SELECT a FROM ArticleEntity a WHERE a.ownerEntity = :ownerEntity AND a.deletedAt IS NULL AND a.disclosure = 'VISIBLE'")
        List<ArticleEntity> findAllByOwnerEntityAndNotDeletedAndDisclosure(@Param("ownerEntity") UserEntity ownerEntity);

        // userEntity, deletedAt == Null
        @Query("SELECT a FROM ArticleEntity a WHERE a.ownerEntity = :ownerEntity AND a.deletedAt IS NULL")
        List<ArticleEntity> findAllByOwnerEntityAndNotDeleted(@Param("ownerEntity") UserEntity ownerEntity);

        /**
         * 휴지통 조회
         */
        @Query("SELECT a FROM ArticleEntity a WHERE a.ownerEntity = :ownerEntity AND a.deletedAt IS NOT NULL")
        Page<ArticleEntity> findAllByOwnerEntityAndDeleted(@Param("ownerEntity") UserEntity ownerEntity, Pageable pageable);

        /**
         * 별자리 공개여부와 해당 별자리 공유여부
         * TODO : 검토 필요
         */
        @Query("SELECT a FROM ArticleEntity a WHERE a.constellationEntity = :constellationEntity AND (a.disclosure = 'VISIBLE' OR a.ownerEntity = :userEntity)")
        List<ArticleEntity> findAllByConstellationEntity(@Param("constellationEntity")ConstellationEntity constellationEntity, @Param("userEntity")UserEntity userEntity);
}

