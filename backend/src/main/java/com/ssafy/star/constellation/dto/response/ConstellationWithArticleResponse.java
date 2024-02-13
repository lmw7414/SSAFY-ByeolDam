package com.ssafy.star.constellation.dto.response;

import com.ssafy.star.article.dto.HoverArticle;
import com.ssafy.star.constellation.dto.ConstellationUser;
import com.ssafy.star.constellation.dto.ConstellationWithArticle;

import java.time.LocalDateTime;
import java.util.List;

public record ConstellationWithArticleResponse(
        Long id,
        String name,
        Long contourId,
        long hits,
        String description,
        List<ConstellationUser> constellationUserEntities,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        List<HoverArticle> hoverArticles
) {
    public static ConstellationWithArticleResponse fromConstellationWithArticle(ConstellationWithArticle dto) {
        return new ConstellationWithArticleResponse(
                dto.id(),
                dto.name(),
                dto.contourId(),
                dto.hits(),
                dto.description(),
                dto.constellationUsers(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.hoverArticles()
        );
    }
}
