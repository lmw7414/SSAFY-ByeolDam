package com.ssafy.star.constellation.dto;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;

import java.time.LocalDateTime;
import java.util.List;


public record Constellation (

    Long id,
    String name,
    Long contourId,
    long hits,
    String description,
    List<ConstellationUserEntity> constellationUserEntities,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    LocalDateTime deletedAt
)
{
    public static Constellation of(
            Long id,
            String name,
            Long contourId,
            long hits,
            String description,
            List<ConstellationUserEntity> constellationUserEntities,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            LocalDateTime deletedAt
    ) {
        return new Constellation(
                id,
                name,
                contourId,
                hits,
                description,
                constellationUserEntities,
                createdAt,
                modifiedAt,
                deletedAt
        );
    }

    public static Constellation fromEntity(ConstellationEntity entity){
        return new Constellation(
                entity.getId(),
                entity.getName(),
                entity.getContourId(),
                entity.getHits(),
                entity.getDescription(),
                entity.getConstellationUserEntities(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt()
        );
    }

    public ConstellationEntity toEntity(){
        return ConstellationEntity.of(
                name,
                description
        );
    }
}

