package com.ssafy.star.global.auth.config;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.global.auth.dto.BoardPrincipal;
import com.ssafy.star.user.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthenticationConfig {
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return email -> userService
                .loadUserByEmail(email)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("email : %s not founded")));

    }
}
