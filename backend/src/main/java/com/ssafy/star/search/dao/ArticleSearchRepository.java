package com.ssafy.star.search.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.common.types.DisclosureType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleSearchRepository extends JpaRepository<ArticleEntity, Long> {

    // 검색 - 제목
    Page<ArticleEntity> findByTitleContainingAndDisclosureAndDeletedAtIsNull(String keyword, DisclosureType disclosure, Pageable pageable);

    // 검색 - 해시태그
    Page<ArticleEntity> findByArticleHashtagRelationEntities_ArticleHashtagEntity_TagNameAndDisclosureAndDeletedAtIsNull(String keyword, DisclosureType disclosure, Pageable pageable);
}
