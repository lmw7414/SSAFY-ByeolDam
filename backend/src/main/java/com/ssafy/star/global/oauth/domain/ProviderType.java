package com.ssafy.star.global.oauth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver"),
    LOCAL("default");

    private final String registrationId;
}
