package com.ssafy.star.search.application;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.search.dao.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.star.common.types.DisclosureType.VISIBLE;


@RequiredArgsConstructor
@Transactional
@Service
public class ArticleSearchService {

    private final ArticleSearchRepository articleSearchRepository;

    public Page<ArticleEntity> titleSearch(String keyword, Pageable pageable) {
        return articleSearchRepository.findByTitleContainingAndDisclosureAndDeletedAtIsNull(keyword, VISIBLE, pageable);
    }

    public Page<ArticleEntity> hashtagSearch(String keyword, Pageable pageable) {
        return articleSearchRepository.findByArticleHashtagRelationEntities_ArticleHashtagEntity_TagNameAndDisclosureAndDeletedAtIsNull(keyword, VISIBLE, pageable);
    }

}
