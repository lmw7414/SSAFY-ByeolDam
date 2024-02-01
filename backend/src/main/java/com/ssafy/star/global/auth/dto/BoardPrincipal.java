package com.ssafy.star.global.auth.dto;

import com.ssafy.star.user.dto.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public record BoardPrincipal(
        Long id,
        String email,
        String password,
        String name,
        String nickname,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt
) implements UserDetails {

    public static BoardPrincipal of(Long id, String email, String password, String name, String nickname, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        return new BoardPrincipal(id, email, password, name, nickname, createdAt, modifiedAt, deletedAt);
    }

    public static BoardPrincipal from(User dto) {
        return BoardPrincipal.of(
                dto.id(),
                dto.email(),
                dto.password(),
                dto.name(),
                dto.nickname(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.deletedAt()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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
}
