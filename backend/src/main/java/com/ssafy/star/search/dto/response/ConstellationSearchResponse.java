package com.ssafy.star.search.dto.response;

import com.ssafy.star.contour.dto.ContourResponse;

import java.time.LocalDateTime;

public record ConstellationSearchResponse(
        Long id,
        String name,
        ContourResponse contourResponse,
        long hits,
        String adminNickname,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
