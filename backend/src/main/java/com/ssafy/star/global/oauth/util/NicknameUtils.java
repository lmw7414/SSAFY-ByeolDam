package com.ssafy.star.global.oauth.util;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NicknameUtils {

    public static String createRandomNickname(String email) {
        if (email == null && email.isEmpty()) {
            throw new ByeolDamException(ErrorCode.UNSUITABLE_EMAIL);
        }
        LocalDateTime dateTime = LocalDateTime.now();
        String id = email.substring(0, email.indexOf('@'));
        String nick = dateTime.format(DateTimeFormatter.ofPattern("mmssSSS"));
        return id + nick;
    }
}
