package com.ssafy.star.constellation.dto;

import com.ssafy.star.article.dto.HoverArticle;
import com.ssafy.star.contour.dto.Contour;

import java.time.LocalDateTime;
import java.util.List;

public record ConstellationWithArticle(
        Long id,
        String name,
        Contour contour,
        long hits,
        String description,
        List<ConstellationUser> constellationUsers,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        List<HoverArticle> hoverArticles
) {
    public static ConstellationWithArticle of(
            Long id,
            String name,
            Contour contour,
            long hits,
            String description,
            List<ConstellationUser> constellationUsers,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            List<HoverArticle> hoverArticles
    ) {
        return new ConstellationWithArticle(
                id,
                name,
                contour,
                hits,
                description,
                constellationUsers,
                createdAt,
                modifiedAt,
                hoverArticles
        );
    }
}
