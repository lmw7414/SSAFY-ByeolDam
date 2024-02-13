package com.ssafy.star.constellation.dto;

import com.ssafy.star.article.dto.HoverArticle;
import com.ssafy.star.constellation.domain.ConstellationEntity;

import java.time.LocalDateTime;
import java.util.List;

public record ConstellationWithArticle(
        Long id,
        String name,
        Long contourId,
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
            Long contourId,
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
                contourId,
                hits,
                description,
                constellationUsers,
                createdAt,
                modifiedAt,
                hoverArticles
        );
    }

    public static ConstellationWithArticle fromEntity(ConstellationEntity constellationEntity) {
        return new ConstellationWithArticle(
                constellationEntity.getId(),
                constellationEntity.getName(),
                constellationEntity.getContourId(),
                constellationEntity.getHits(),
                constellationEntity.getDescription(),
                constellationEntity.getConstellationUserEntities().stream().map(ConstellationUser::fromEntity).toList(),
                constellationEntity.getCreatedAt(),
                constellationEntity.getModifiedAt(),
                constellationEntity.getArticleEntities().stream().map(HoverArticle::fromEntity).toList()
        );
    }
}
