package com.ssafy.star.user.dto.response;

import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.global.oauth.domain.ProviderType;
import com.ssafy.star.user.domain.RoleType;
import com.ssafy.star.user.dto.User;

import java.time.LocalDate;

// 프로필 자세히 보기에서 사용할 Response
public record UserProfileResponse(
        String imageUrl,
        String email,
        ProviderType providerType,
        RoleType roleType,
        String name,
        String nickname,
        String memo,
        DisclosureType disclosureType,
        LocalDate birthday,
        Integer articleCounts,
        Integer constellationCounts,
        Long Followers,
        Long Followings

) {

    public static UserProfileResponse fromUser(User dto, int articleCounts, int constellationCounts, Long followers, Long followings) {
        return new UserProfileResponse(
                dto.image() == null ? "" : dto.image().url(),
                dto.email(),
                dto.providerType(),
                dto.roleType(),
                dto.name(),
                dto.nickname(),
                dto.memo(),
                dto.disclosureType(),
                dto.birthday(),
                articleCounts,
                constellationCounts,
                followers,
                followings
        );
    }
}
