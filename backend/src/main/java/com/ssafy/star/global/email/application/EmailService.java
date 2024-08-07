package com.ssafy.star.global.email.application;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail, String authCode) {
        String title = "[별을담다] 안녕하세요. 이메일 인증 코드입니다.";
        String content = "[별을 담다]서비스에 방문해주셔서 감사합니다." +
                "\n\n" +
                "인증번호는 " + authCode +
                "입니다." +
                "\n" +
                "5분안에 입력해주세요." +
                "\n\n" +
                "감사합니다." +
                "\n\n" +
                "- 별을 담다 서비스팀 -";
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, content);

        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, title: {}, text: {}", toEmail, title, content);
            throw new ByeolDamException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
