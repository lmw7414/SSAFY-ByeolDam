package com.ssafy.star.constellation.dto.response;

import com.ssafy.star.constellation.ConstellationUserRole;

public record ConstellationForUserResponse(
        String imageUrl,
        String name,
        String nickname,
        ConstellationUserRole constellationUserRole
) {
}
