package com.ssafy.star.user.dto.request;

import com.ssafy.star.common.types.DisclosureType;

import java.time.LocalDate;

public record UserModifyRequest(
        String password,
        String name,
        String nickname,
        String memo,
        DisclosureType disclosureType,
        LocalDate birthday

) {
}
