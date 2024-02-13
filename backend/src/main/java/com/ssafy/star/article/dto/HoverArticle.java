package com.ssafy.star.article.dto;

import com.ssafy.star.article.domain.ArticleEntity;

public record HoverArticle(
        Long id,
        String articleThumbnail
) {
    public static HoverArticle fromEntity(ArticleEntity entity) {
        return new HoverArticle(
                entity.getId(),
                entity.getImageEntity().getThumbnailUrl()
        );
    }
}
