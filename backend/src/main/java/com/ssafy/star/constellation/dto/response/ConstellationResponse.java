package com.ssafy.star.constellation.dto.response;

import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.constellation.dto.ConstellationUser;

import java.time.LocalDateTime;
import java.util.List;


public record ConstellationResponse (
        Long id,
        String name,
        String title,
        Long contourId,
        long hits,
        List<ConstellationUser> constellationUsers,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {

    public static ConstellationResponse fromConstellation(Constellation dto) {
        return new ConstellationResponse(
                dto.id(),
                dto.name(),
                dto.title(),
                dto.contourId(),
                dto.hits(),
                dto.constellationUsers(),
                dto.createdAt(),
                dto.modifiedAt()
        );
    }
}