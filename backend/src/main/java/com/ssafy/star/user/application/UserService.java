package com.ssafy.star.user.application;

import com.ssafy.star.common.config.properties.AppProperties;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.global.auth.util.AuthToken;
import com.ssafy.star.global.auth.util.AuthTokenProvider;
import com.ssafy.star.global.email.Repository.EmailCacheRepository;
import com.ssafy.star.global.email.application.EmailService;
import com.ssafy.star.global.oauth.domain.ProviderType;
import com.ssafy.star.global.oauth.util.CookieUtils;
import com.ssafy.star.global.oauth.util.HeaderUtils;
import com.ssafy.star.user.domain.FollowEntity;
import com.ssafy.star.user.domain.RoleType;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.domain.UserRefreshToken;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserCacheRepository;
import com.ssafy.star.user.repository.UserRefreshTokenRepository;
import com.ssafy.star.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final EmailCacheRepository emailCacheRepository;
    private final FollowRepository followRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;

    @Value("${mail.auth-code-expired-ms}")
    private Long mailExpiredMs;
    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    //TODO : 로그인 시 응답 -> 유저 리스폰스 + 토큰 값

    public Optional<User> loadUserByEmail(String email) {
        return Optional.ofNullable(userCacheRepository.getUser(email)
                .orElseGet(() ->
                        userRepository.findByEmail(email)
                                .map(User::fromEntity)
                                .orElseThrow(() ->
                                        new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email))
                                )
                )
        );
    }

    // 이메일 인증 요청 시도
    @Transactional
    public void sendCodeByEmail(String email) {
        validateEmailPattern(email);
        checkEmailExistenceOrException(email);
        String authCode = generateMailCode();  // 코드 생성

        emailService.sendEmail(email, authCode);  // 메일 보내기
        emailCacheRepository.setEmailCode(email, authCode, mailExpiredMs);  // 레디스에 저장
    }

    // 이메일 인증코드 검증
    public boolean verifyEmailCode(String email, String userAuthCode) {
        validateEmailPattern(email);
        checkEmailExistenceOrException(email);
        String authCode = emailCacheRepository.getEmailCode(email).orElseThrow(() -> new ByeolDamException(ErrorCode.NEVER_ATTEMPT_EMAIL_AUTH));

        if (!userAuthCode.equals(authCode)) {
            throw new ByeolDamException(ErrorCode.INVALID_EMAIL_CODE);
        }
        return true;
    }


    // 이메일 중복 체크(참일 경우 중복되는 이메일 없음)
    public boolean checkDuplicateEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    // 닉네임 중복 체크(참일 경우 중복되는 닉네임 없음)
    public boolean checkDuplicateNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    // 로그인
    public String login(HttpServletRequest request, HttpServletResponse response, String email, String password) {
        // 회원가입 여부 체크
        User user = loadUserByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email))
        );
        userCacheRepository.setUser(user);

        // 비밀번호 체크
        if (!encoder.matches(password, user.password())) {
            throw new ByeolDamException(ErrorCode.INVALID_PASSWORD);
        }

        /**
         * 1. JWT 토큰 생성
         * 2. 리프레시 토큰 생성
         * 3. 유저 - 리프레시 토큰이 있는지 확인
         * 4. 없으면 새로 등록
         * 5. 있으면 DB에 업데이트
         * 6. 기존 쿠키 삭제하고 새로 추가
         * 7. JWT 토큰 리턴
         */
        AuthToken accessToken = tokenProvider.createAuthToken(email, user.nickname(), user.roleType().getCode(), appProperties.getAuth().getTokenExpiry());
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(appProperties.getAuth().getTokenSecret(), refreshTokenExpiry);

        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(email);
        if (userRefreshToken == null) { // 토큰이 없는 경우. 새로 등록
            userRefreshToken = new UserRefreshToken(email, refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {  // 토큰 발견 -> 리프레시 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        // 토큰 생성 후 리턴
        return accessToken.getToken();
    }

    // 리프레시 토큰

    /**
     * 1. 액세스 토큰 기존 헤더에서 가져오기
     * 2. 액세스 토큰 (String)-> (Token)으로 변환
     * 3. 유효한 토큰인지 검증
     * 4. 만료된 토큰인지 검증
     * 5. claims에서 이메일 가져오기
     * 6. Claims에서 role타입 가져오기
     * 7. 프론트로 부터 쿠키에서 리프레시 토큰 가져오기
     * 8. 유효한지 확인
     * 9. 유저-리프레시 레포에서 토큰 가져오기 - 없을 경우 에러 - 로그인할 때 만들어지기 때문에 없을리 없음
     * 10. 새로운 액세스 토큰 발급
     * 11. 리프레시 토큰이 3일 이하로 남았을 경우 리프레시 토큰 갱신
     * 12. DB에 업데이트
     * 13. 액세스 토큰 리턴
     */

    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = HeaderUtils.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

        if (!authToken.validate()) {
            throw new ByeolDamException(ErrorCode.INVALID_TOKEN);
        }

        Claims claims = authToken.getExpiredClaims();
        if (claims == null) {
            return accessToken;  // 아직 만료 안됨
        }

        String email = authToken.getUserEmail();
        String nickname = authToken.getUserNickname();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        //refresh token
        String refreshToken = CookieUtils.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse(null);
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (authRefreshToken.validate()) {
            throw new ByeolDamException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(email, refreshToken);
        if (userRefreshToken == null) {
            throw new ByeolDamException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(email, nickname, roleType.getCode(), appProperties.getAuth().getTokenExpiry());

        long validTime = authRefreshToken.extractClaims().getExpiration().getTime() - now.getTime();

        //refresh 토큰 기간이 3일 이하일 경우 새로 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    appProperties.getAuth().getRefreshTokenExpiry()
            );

            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;
            CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtils.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }
        return newAccessToken.getToken();
    }

    // 회원가입 - 이메일 인증
    @Transactional
    public User join(String email, String password, String name, String nickname) {
        validateEmailPattern(email);
        checkEmailExistenceOrException(email);
        if (satisfyNickname(nickname)) {
            UserEntity userEntity = UserEntity.of(email, ProviderType.LOCAL, encoder.encode(password), name, nickname);
            return User.fromEntity(userRepository.save(userEntity));
        }
        throw new ByeolDamException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    // 회원가입 - 소셜로그인
    @Transactional
    public User join(String email, ProviderType providerType, String password, String name, String nickname) {
        checkEmailExistenceOrException(email);
        if (satisfyNickname(nickname)) {
            UserEntity userEntity = UserEntity.of(email, providerType, password, name, nickname);
            return User.fromEntity(userRepository.save(userEntity));
        }
        throw new ByeolDamException(ErrorCode.INTERNAL_SERVER_ERROR);
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
    public void updateMyProfile(
            String email,
            String password,
            String name,
            String nickname,
            String memo,
            DisclosureType disclosureType,
            LocalDate birthday) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", email)));
        //닉네임 중복체크
        if (satisfyNickname(nickname)) {
            userEntity.setNickname(nickname);
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

        userRepository.saveAndFlush(userEntity);
    }

    //회원 탈퇴
    @Transactional
    public void delete(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", email)));
        userRepository.delete(userEntity);
    }

    //이메일 인증코드 생성
    private String generateMailCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("이메일 인증 코드 생성 중 에러 발생 : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean satisfyNickname(String nickname) {
        validateNicknamePattern(nickname);
        userRepository.findByNickname(nickname).ifPresent(it -> {
                    throw new ByeolDamException(ErrorCode.DUPLICATED_USER_NICKNAME, String.format("%s is duplcated", nickname));
                }
        );
        return true;
    }

    private void validateEmailPattern(String email) {
        final String REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern regex = Pattern.compile(REGEX);
        Matcher matcher = regex.matcher(email);

        if (!matcher.matches()) {
            throw new ByeolDamException(ErrorCode.UNSUITABLE_EMAIL, String.format("%s doesn't meet the conditions", email));
        }
    }

    private void validateNicknamePattern(String nickname) {
        //오직 소문자, 숫자, 4자 이상의 패턴만 가능
        final String REGEX = "^(?=.*[a-z])[a-z0-9_]{4,}$";
        Pattern regex = Pattern.compile(REGEX);
        Matcher matcher = regex.matcher(nickname);

        if (!matcher.matches()) {
            throw new ByeolDamException(ErrorCode.UNSUITABLE_NICKNAME, String.format("%s doesn't meet the conditions", nickname));
        }
    }

    // 해당 이메일이 이미 존재하는 이메일인지 확인 체크
    private void checkEmailExistenceOrException(String email) {
        userRepository.findByEmail(email).ifPresent(it -> {
                    throw new ByeolDamException(ErrorCode.DUPLICATED_USER_EMAIL, String.format("%s is duplicated", email));
                }
        );
    }

}
