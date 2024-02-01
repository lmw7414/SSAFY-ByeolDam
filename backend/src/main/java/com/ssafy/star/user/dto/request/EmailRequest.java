package com.ssafy.star.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmailRequest(
        @Schema(description = "사용자 이메일", nullable = false, example = "example@example.com")
        String email
) {
}