package com.ssafy.star.search.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.common.types.DisclosureType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleSearchRepository extends JpaRepository<ArticleEntity, Long> {

    // 검색 - 제목
    List<ArticleEntity> findByTitleContainingAndDisclosureAndDeletedAtIsNull(String keyword, DisclosureType disclosure, Sort sort);

    // 검색 - 해시태그
    List<ArticleEntity> findByArticleHashtagRelationEntities_ArticleHashtagEntity_TagNameAndDisclosureAndDeletedAtIsNull(String keyword, DisclosureType disclosure, Sort sort);
}
