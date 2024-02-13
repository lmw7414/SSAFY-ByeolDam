package com.ssafy.star.contour.dto;

import com.ssafy.star.contour.domain.ContourEntity;

import java.util.List;

public record Contour(
        String originUrl,
        String thumbUrl,
        String cThumbUrl,
        List<List<List<Integer>>> contoursList,
        List<List<Integer>> ultimate

) {
    public static Contour fromEntity(ContourEntity entity) {
        return new Contour(
                entity.getOriginUrl(),
                entity.getThumbUrl(),
                entity.getCThumbUrl(),
                entity.getContoursList(),
                entity.getUltimate()
        );
    }

    public ContourEntity toEntity() {
        return ContourEntity.of(
                this.originUrl,
                this.thumbUrl,
                this.cThumbUrl,
                this.contoursList,
                this.ultimate

        );
    }
}
