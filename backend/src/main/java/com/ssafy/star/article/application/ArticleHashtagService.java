package com.ssafy.star.article.application;

import com.ssafy.star.article.dao.ArticleHashtagRepository;
import com.ssafy.star.article.domain.ArticleHashtagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleHashtagService {
    private final ArticleHashtagRepository articleHashtagRepository;

    public Optional<ArticleHashtagEntity> findByTagName(String tagName) {

        return articleHashtagRepository.findByTagName(tagName);
    }

    public ArticleHashtagEntity save(String tagName) {

        return articleHashtagRepository.save(
                ArticleHashtagEntity.builder()
                        .tagName(tagName)
                        .build());
    }
}
