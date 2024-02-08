package com.ssafy.star.ai.dto;

import java.util.List;
import java.util.Map;

public record AiResponse(
        Map<String, Object> result
){
    public record body(
       Long imageHeight,
       Long imageWidth,
       List<Long> boundingBox,
       List<Long> contours,
       String id
    ){}
}
