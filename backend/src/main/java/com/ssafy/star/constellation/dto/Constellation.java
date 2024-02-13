package com.ssafy.star.constellation.dto;

import com.ssafy.star.constellation.domain.ConstellationEntity;

import java.time.LocalDateTime;
import java.util.List;


public record Constellation (

    Long id,
    String name,
    String title,
    Long contourId,
    long hits,
    List<ConstellationUser> constellationUsers,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
)
{
    public static Constellation of(
            Long id,
            String name,
            String title,
            Long contourId,
            long hits,
            List<ConstellationUser> constellationUsers,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        return new Constellation(
                id,
                name,
                title,
                contourId,
                hits,
                constellationUsers,
                createdAt,
                modifiedAt
        );
    }

    public static Constellation fromEntity(ConstellationEntity entity){
        return new Constellation(
                entity.getId(),
                entity.getName(),
                entity.getTitle(),
                entity.getContourId(),
                entity.getHits(),
                entity.getConstellationUserEntities().stream().map(ConstellationUser::fromEntity).toList(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    public ConstellationEntity toEntity(){
        return ConstellationEntity.of(
                title,
                name
        );
    }
}

