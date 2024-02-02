package com.ssafy.star.global.auth.config;

import com.ssafy.star.common.exception.CustomAuthenticationEntryPoint;
import com.ssafy.star.global.auth.config.filter.JwtTokenFilter;
import com.ssafy.star.user.application.UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    @Value("${jwt.secret-key}")
    private String key;

    //    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers(HttpMethod.POST, "/api/*/users/join", "/api/v1/users/login");
//    }
    private static final String[] AUTH_WHITELIST = {
            "/api/**", "/graphiql", "/graphql",
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html"
    };


    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedOrigin("http://i10b309.p.ssafy.io:5173");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L); //preflight 결과를 1시간동안 캐시에 저장
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(configurationSource()))
                .csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/api/*/users/join", "/api/*/users/login", "/api/*/users/check-email", "/api/*/users/check-nickname").permitAll()
                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll().requestMatchers(AUTH_WHITELIST).permitAll()
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
