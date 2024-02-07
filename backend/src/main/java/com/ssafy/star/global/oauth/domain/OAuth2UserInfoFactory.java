package com.ssafy.star.global.oauth.domain;

import com.ssafy.star.global.oauth.domain.google.GoogleOAuth2UserInfo;
import com.ssafy.star.global.oauth.domain.kakao.KakaoOAuth2UserInfo;
import com.ssafy.star.global.oauth.domain.naver.NaverOAuth2UserInfo;
import com.ssafy.star.global.oauth.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {


    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE -> {
                return new GoogleOAuth2UserInfo(attributes);
            }
            case KAKAO -> {
                return new KakaoOAuth2UserInfo(attributes);
            }
            case NAVER -> {
                return new NaverOAuth2UserInfo(attributes);
            }
            default -> throw new IllegalArgumentException("Invalid Provider Type");
        }
    }
}
