package com.ssafy.star.user.application;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.user.application.fixture.UserEntityFixture;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입시_정상동작() {
        String email = "email";
        String password = "password";
        String name = "name";
        String nickname = "nickname";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(UserEntityFixture.get(email, password, name, nickname));

        Assertions.assertDoesNotThrow(() -> userService.join(email, password, name, nickname));
    }

    @Test
    void 회원가입시_해당_이메일로_가입한_유저가_이미_있는경우() {
        String email = "email";
        String password = "password";
        String name = "name";
        String nickname = "nickname";
        UserEntity fixture = UserEntityFixture.get(email, password, name, nickname);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(Optional.of(fixture));

        ByeolDamException e = Assertions.assertThrows(ByeolDamException.class, () -> userService.join(email, password, name, nickname));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_EMAIL, e.getErrorCode());
    }

    // 로그인 정상 동작
    @Test
    void 로그인_정상동작() {
        String email = "email";
        String password = "password";
        String name = "name";
        String nickname = "nickname";
        UserEntity fixture = UserEntityFixture.get(email, password, name, nickname);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> userService.login(email, password));
    }

    @Test
    void 로그인시_해당_이메일로_회원가입한_유저가_없는_경우() {
        String email = "email";
        String password = "password";
        String name = "name";
        String nickname = "nickname";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        ByeolDamException e = Assertions.assertThrows(ByeolDamException.class, () -> userService.login(email, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 로그인시_패스워드가_틀린_경우() {
        String email = "email";
        String password = "password";
        String wrongPassword = "wrongPassword";
        String name = "name";
        String nickname = "nickname";
        UserEntity fixture = UserEntityFixture.get(email, password, name, nickname);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(fixture));
        ByeolDamException e = Assertions.assertThrows(ByeolDamException.class, () -> userService.login(email, wrongPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}