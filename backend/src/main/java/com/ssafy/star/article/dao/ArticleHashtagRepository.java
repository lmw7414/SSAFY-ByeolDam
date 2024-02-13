package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleHashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleHashtagRepository extends JpaRepository<ArticleHashtagEntity, Long> {
    Optional<ArticleHashtagEntity> findByTagName(String tagName);
}
