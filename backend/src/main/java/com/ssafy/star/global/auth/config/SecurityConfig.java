package com.ssafy.star.global.auth.config;

import com.ssafy.star.common.exception.CustomAuthenticationEntryPoint;
import com.ssafy.star.global.auth.config.filter.JwtTokenFilter;
import com.ssafy.star.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    @Value("${jwt.secret-key}")
    private String key;

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers(HttpMethod.POST, "/api/*/users/join", "/api/v1/users/login");
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/api/*/users/join", "/api/*/users/login", "/api/*/users/check-email", "/api/*/users/check-nickname").permitAll()
                                .requestMatchers("/api/**").authenticated()
                ).sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionManager ->
                        exceptionManager.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return http.build();

    }
}
