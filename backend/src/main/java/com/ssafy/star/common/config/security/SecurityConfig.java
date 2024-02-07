package com.ssafy.star.common.config.security;

import com.ssafy.star.common.exception.CustomAuthenticationEntryPoint;
import com.ssafy.star.global.auth.config.AuthenticationConfig;
import com.ssafy.star.global.oauth.service.CustomOAuth2UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService oAuth2UserService;
    private final AuthenticationConfig authenticationConfig;

    private static final String[] AUTH_WHITELIST = {
            "/graphiql", "/graphql",
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html"
    };

    private static final String[] OPEN_API_URLS = {
            "/api/*/users/join",
            "/api/*/users/login",
            "/api/*/users/check-email",
            "/api/*/users/check-nickname",
            "/api/*/email/**",
            "/api/*/users/{nickname}",
            "/api/*/{nickname}/count-followers",
            "/api/*/{nickname}/count-followings"
    };

    @Bean
    public CorsConfigurationSource configurationSource() {
        return request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173", "http://i10b309.p.ssafy.io:5173"));
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Authorization-refresh", "Cache-Control", "Content-Type"));

            /* 응답 헤더 설정 추가*/
            corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh"));
            corsConfiguration.setMaxAge(3600L); //preflight 결과를 1시간동안 캐시에 저장
            return corsConfiguration;
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(authenticationConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(OPEN_API_URLS).permitAll()
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .requestMatchers("/api/**").authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(configure -> configure
                        .authorizationEndpoint(config -> config
                                .authorizationRequestRepository(authenticationConfig.oAuth2AuthorizationRequestBasedOnCookieRepository())
                                .baseUri("/oauth2/authorization")
                        )
                        .userInfoEndpoint(config -> config.userService(oAuth2UserService))
                        .redirectionEndpoint(config -> config.baseUri("/*/oauth2/code/*"))
                        .successHandler(authenticationConfig.oAuth2AuthenticationSuccessHandler())
                        .failureHandler(authenticationConfig.oAuth2AuthenticationFailureHandler())
                )
                .exceptionHandling(exceptionManager ->
                        exceptionManager.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .addFilterBefore(authenticationConfig.tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(config -> config
                        .logoutUrl("/logout")
                        .addLogoutHandler(authenticationConfig.logoutSuccessHandler())
                        .deleteCookies("refresh_token")
                );

        return http.build();
    }

}
