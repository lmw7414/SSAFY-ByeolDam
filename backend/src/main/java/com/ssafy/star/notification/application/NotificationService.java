package com.ssafy.star.notification.application;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.notification.domain.NotificationEntity;
import com.ssafy.star.notification.dto.NotificationArgs;
import com.ssafy.star.notification.dto.NotificationNoti;
import com.ssafy.star.notification.dto.NotificationType;
import com.ssafy.star.notification.repository.EmitterRepository;
import com.ssafy.star.notification.repository.NotificationRepository;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final static String notification_NAME = "Notification";

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    // topic 발행 시 자동 실행
    public void send(NotificationType type, NotificationArgs args, Long receiverId) {
        UserEntity userEntity = userRepository.findById(receiverId).orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND));
        String fromNickname = userRepository.findById(args.getFromUserId()).orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND)).getNickname();
        NotificationEntity notificationEntity = NotificationEntity.of(type, args, userEntity, fromNickname);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        notificationRepository.save(notificationEntity);
        emitterRepository.get(receiverId).ifPresentOrElse(it -> {
                    try {
                        it.send(SseEmitter.event()
                                .id(notificationEntity.getId().toString())
                                .name(notification_NAME)
                                .data(new NotificationNoti()));
                    } catch (IOException exception) {
                        emitterRepository.delete(receiverId);
                        throw new ByeolDamException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
                    }
                },
                () -> log.info("No emitter founded")
        );
    }

    // 유저 로그인 시 emitter 생성 및 활성화 (로그인시 자동실행 예정)
    public SseEmitter connectNotification(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND));
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userEntity.getId(), emitter);
        emitter.onCompletion(() -> emitterRepository.delete(userEntity.getId()));
        emitter.onTimeout(() -> emitterRepository.delete(userEntity.getId()));

        try {
            emitter.send(SseEmitter.event()
                    .id("id")
                    .name(notification_NAME)
                    .data("connect completed"));
        } catch (IOException exception) {
            throw new ByeolDamException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
        }
        return emitter;
    }

}
