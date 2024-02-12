package com.ssafy.star.user.dto.response;

import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.global.oauth.domain.ProviderType;
import com.ssafy.star.user.domain.RoleType;
import com.ssafy.star.user.dto.User;

import java.time.LocalDate;

// 로그인, 회원정보 수정, 프로필 이미지 수정에서 사용할 Response
public record UserDefaultResponse(
        String imageUrl,
        String email,
        ProviderType providerType,
        RoleType roleType,
        String name,
        String nickname,
        DisclosureType disclosureType,
        Integer articleCounts,
        Integer constellationCounts,
        Long followers,
        Long followings
) {

    public static UserDefaultResponse fromUser(User dto, int articleCounts, int constellationCounts,Long followers, Long followings) {
        return new UserDefaultResponse(
                dto.image() == null ? "" : dto.image().url(),
                dto.email(),
                dto.providerType(),
                dto.roleType(),
                dto.name(),
                dto.nickname(),
                dto.disclosureType(),
                articleCounts,
                constellationCounts,
                followers,
                followings
        );
    }
}
