package com.ssafy.star.search.application;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.search.dao.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.star.common.types.DisclosureType.VISIBLE;


@RequiredArgsConstructor
@Transactional
@Service
public class ArticleSearchService {

    private final ArticleSearchRepository articleSearchRepository;

    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    public List<ArticleEntity> titleSearch(String keyword) {
        return articleSearchRepository.findByTitleContainingAndDisclosureAndDeletedAtIsNull(keyword, VISIBLE, sort);
    }

    public List<ArticleEntity> hashtagSearch(String keyword) {
        return articleSearchRepository.findByArticleHashtagRelationEntities_ArticleHashtagEntity_TagNameAndDisclosureAndDeletedAtIsNull(keyword, VISIBLE, sort);
    }

}
