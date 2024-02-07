package com.ssafy.star.global.auth.dto;

import com.ssafy.star.global.oauth.domain.ProviderType;
import com.ssafy.star.user.domain.RoleType;
import com.ssafy.star.user.dto.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record BoardPrincipal(
        String email,
        String password,
        String name,
        String nickname,
        ProviderType providerType,
        RoleType roleType,
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt

) implements UserDetails, OAuth2User, OidcUser {

    public static BoardPrincipal of(String email, String password, String name, String nickname, ProviderType providerType, RoleType roleType, Map<String, Object> attributes, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        return new BoardPrincipal(
                email,
                password,
                name,
                nickname,
                providerType,
                roleType,
                Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.getCode())),
                attributes,
                createdAt,
                modifiedAt,
                deletedAt);
    }

    public static BoardPrincipal of(String email, String password, String name, String nickname, ProviderType providerType, RoleType roleType, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        return of(email, password, name, nickname, providerType, roleType, Map.of(), createdAt, modifiedAt, deletedAt);
    }

    public static BoardPrincipal from(User dto) {
        return of(
                dto.email(),
                dto.password(),
                dto.name(),
                dto.nickname(),
                dto.providerType(),
                dto.roleType(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.deletedAt()
        );
    }

    public static BoardPrincipal from(User dto, Map<String, Object> attributes) {
        return of(
                dto.email(),
                dto.password(),
                dto.name(),
                dto.nickname(),
                dto.providerType(),
                dto.roleType(),
                attributes,
                dto.createdAt(),
                dto.modifiedAt(),
                dto.deletedAt()
        );
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isEnabled() {
        return this.deletedAt == null;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
