package com.ssafy.star.constellation.dto;

import com.ssafy.star.constellation.ConstellationUserRole;

public record ConstellationForUser(
        String imageUrl,
        String name,
        String nickname,
        ConstellationUserRole constellationUserRole
) {
    public static ConstellationForUser of(
            String imageUrl,
            String name,
            String nickname,
            ConstellationUserRole constellationUserRole
    ) {
        return new ConstellationForUser(
            imageUrl,
            name,
            nickname,
            constellationUserRole
        );
    }
}
