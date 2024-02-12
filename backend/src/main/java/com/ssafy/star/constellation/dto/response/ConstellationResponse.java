package com.ssafy.star.constellation.dto.response;

import com.ssafy.star.constellation.dto.Constellation;

import java.time.LocalDateTime;


public record ConstellationResponse (
        Long Id,
    String name,
    // TODO : 외곽선
    long hits,
    String description,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    LocalDateTime deletedAt

) {

    public static ConstellationResponse fromConstellation(Constellation dto) {
        return new ConstellationResponse(
                dto.id(),
                dto.name(),
                // const.getOutline(),
                dto.hits(),
                dto.description(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.deletedAt()
        );
    }
}