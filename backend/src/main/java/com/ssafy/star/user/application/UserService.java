package com.ssafy.star.user.application;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.global.auth.util.JwtTokenUtils;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public Optional<User> loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::fromEntity);
    }

}
