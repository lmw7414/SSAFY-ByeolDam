package com.ssafy.star.global.oauth.service;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.global.auth.dto.BoardPrincipal;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.UserCacheRepository;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userCacheRepository.getUser(email)
                .orElseGet(() ->
                        userRepository.findByEmail(email)
                                .map(User::fromEntity)
                                .orElseThrow(() ->
                                        new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email))
                                )
                )
        );
        return user.map(BoardPrincipal::from).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email))
        );
    }
}
