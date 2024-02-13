package com.ssafy.star.search.dto.response;

import com.ssafy.star.constellation.dto.ConstellationUser;
import com.ssafy.star.contour.dto.ContourResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ConstellationSearchResponse(
        Long id,
        String name,
        String title,
        ContourResponse contourResponse,
        long hits,
        List<ConstellationUser> constellationUsers,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
