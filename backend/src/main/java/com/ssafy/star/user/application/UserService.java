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
    //TODO : 해당 이메일로  인증코드를 전송하고 확인하는 체크 로직 필요

    // 이메일 중복 체크(참일 경우 중복되는 이메일 없음)
    public boolean checkDuplicateEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    // 닉네임 중복 체크(참일 경우 중복되는 닉네임 없음)
    public boolean checkDuplicateNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    // 로그인
    public String login(String email, String password) {
        // 회원가입 여부 체크
        User user = loadUserByEmail(email).orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", email)));
        // 비밀번호 체크
        if (!encoder.matches(password, user.password())) {
            throw new ByeolDamException(ErrorCode.INVALID_PASSWORD);
        }
        // 토큰 생성 후 리턴
        String token = JwtTokenUtils.generateToken(email, user.nickname(), secretKey, expiredTimeMs);
        return token;
    }

    // 회원가입
    @Transactional
    public User join(String email, String password, String name, String nickname) {
        // 이미 존재하는 계정인지 확인 체크
        userRepository.findByEmail(email).ifPresent(it -> {
                    throw new ByeolDamException(ErrorCode.DUPLICATED_USER_EMAIL, String.format("%s is duplicated", email));
                }
        );
        // 중복된 닉네임인지 체크
        userRepository.findByNickname(nickname).ifPresent(it -> {
                    throw new ByeolDamException(ErrorCode.DUPLICATED_USER_NICKNAME, String.format("%s is duplcated", nickname));
                }
        );
        // 회원가입 진행
        UserEntity userEntity = UserEntity.of(email, encoder.encode(password), name, nickname, null, DisclosureType.VISIBLE, null);
        return User.fromEntity(userRepository.save(userEntity));
    }

    //회원정보 조회
    @Transactional(readOnly = true)
    public User my(String nickName) {
        UserEntity userEntity = userRepository.findByNickname(nickName).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", nickName)));
        return User.fromEntity(userEntity);
    }

    //회원정보 수정
    @Transactional
    public User updateMyProfile(
            String password,
            String name,
            String nickname,
            String memo,
            DisclosureType disclosureType,
            LocalDate birthday) {

        UserEntity userEntity = userRepository.findByNickname(nickname).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", nickname)));
        //닉네임 중복체크
        if (nickname != null) {
            if(!userEntity.getNickname().equals(nickname)) {
                userRepository.findByNickname(nickname).ifPresent(it -> {
                            throw new ByeolDamException(ErrorCode.DUPLICATED_USER_NICKNAME, String.format("%s is duplcated", nickname));
                        }
                );
                userEntity.setName(nickname);
            }
        }
        if (password != null) {
            userEntity.setPassword(encoder.encode(password));
        }
        if (name != null) {
            userEntity.setName(name);
        }
        if (birthday != null) {
            userEntity.setBirthday(birthday);
        }
        if (disclosureType != null) {
            userEntity.setDisclosureType(disclosureType);
        }
        //null이어도 되는 필드
        userEntity.setMemo(memo);
        return User.fromEntity(userRepository.saveAndFlush(userEntity));
    }

    //회원 탈퇴
    @Transactional
    public void delete(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", email)));
        userRepository.delete(userEntity);
    }

}
