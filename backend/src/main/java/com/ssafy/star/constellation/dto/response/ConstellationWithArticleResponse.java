package com.ssafy.star.constellation.dto.response;

import com.ssafy.star.article.dto.HoverArticle;
import com.ssafy.star.constellation.dto.ConstellationUser;
import com.ssafy.star.constellation.dto.ConstellationWithArticle;
import com.ssafy.star.contour.dto.ContourResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ConstellationWithArticleResponse(
        Long id,
        String name,
        ContourResponse contourResponse,
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
                ContourResponse.fromContour(dto.contour()),
                dto.hits(),
                dto.description(),
                dto.constellationUsers(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.hoverArticles()
        );
    }
}
