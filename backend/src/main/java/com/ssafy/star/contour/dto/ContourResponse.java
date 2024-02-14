package com.ssafy.star.contour.dto;

import java.util.List;

public record ContourResponse (
        String thumbUrl,
        String cThumbUrl,
        List<List<Integer>> ultimate
){
    public static ContourResponse fromContour(Contour dto) {
        return new ContourResponse(
                dto.thumbUrl(),
                dto.cThumbUrl(),
                dto.ultimate()
        );
    }
}
