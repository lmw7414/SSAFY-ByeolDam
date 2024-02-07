package com.ssafy.star.global.oauth.service;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.global.auth.dto.BoardPrincipal;
import com.ssafy.star.global.oauth.domain.OAuth2UserInfo;
import com.ssafy.star.global.oauth.domain.OAuth2UserInfoFactory;
import com.ssafy.star.global.oauth.domain.ProviderType;
import com.ssafy.star.global.oauth.exception.OAuth2AuthenticationProcessingException;
import com.ssafy.star.global.oauth.util.NicknameUtils;
import com.ssafy.star.user.application.UserService;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // 스프링 시큐리티 OAuth2LoginAuthenticationFilter에서 시작된 OAuth2인증 과정 중에 호출된다.
    // 호출 시점 : 액세스 토큰을 OAuth2 제공자로부터 받았을 때.
    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return process(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Access Token을 얻고 난 후
     * 토큰으로 유저 정보 받아오기
     * 해당 이메일로 된 계정이 없는 경우 -> 회원가입
     * 해당 이메일로 된 계정이 있는 경우 -> 토큰 발급
     */
    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, oAuth2User.getAttributes());

        if (!StringUtils.hasText(userInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        // 이미 가입한 유저인지 체크
        UserEntity savedUser = userRepository.findByEmail(userInfo.getEmail()).orElse(null);
        if (savedUser == null) {  // 없는 유저 회원가입 필요
            String dummyPassword = encoder.encode("{bcrypt}" + UUID.randomUUID());
            userService.join(userInfo.getEmail(), providerType, dummyPassword, userInfo.getName(), NicknameUtils.createRandomNickname(userInfo.getEmail()));
        }
        return userRepository.findByEmail(userInfo.getEmail())
                .map(User::fromEntity)
                .map(user -> BoardPrincipal.from(user, oAuth2User.getAttributes()))
                .orElseThrow(() ->
                        new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userInfo.getEmail()))
                );
    }

}
