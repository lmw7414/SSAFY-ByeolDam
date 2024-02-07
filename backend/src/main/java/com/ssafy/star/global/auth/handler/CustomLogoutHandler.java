package com.ssafy.star.global.auth.handler;

import com.ssafy.star.global.oauth.util.CookieUtils;
import com.ssafy.star.user.domain.UserRefreshToken;
import com.ssafy.star.user.repository.UserCacheRepository;
import com.ssafy.star.user.repository.UserRefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final UserCacheRepository userCacheRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final static String REFRESH_TOKEN = "refresh_token";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String email = authentication.getName();
        // 레디스 정보 삭제
        userCacheRepository.deleteUser(email);
        // 리프레시 토큰 삭제
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(email);
        log.info("user Refresh 토큰 정보 : {}",userRefreshToken);
        userRefreshTokenRepository.delete(userRefreshToken);

        // 헤더 토큰 삭제
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        //response.sendRedirect("/");
    }


    //@Override
    //public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String email = authentication.getName();
//        // 레디스 정보 삭제
//        userCacheRepository.deleteUser(email);
//        // 리프레시 토큰 삭제
//        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(email);
//        log.info("user Refresh 토큰 정보 : {}",userRefreshToken);
//        userRefreshTokenRepository.delete(userRefreshToken);
//
//        // 헤더 토큰 삭제
//        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
//        response.sendRedirect("/");
  //  }

}
