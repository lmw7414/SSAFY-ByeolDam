package com.ssafy.star.notification.dto.response;

import com.ssafy.star.notification.dto.NotificationDto;

import java.time.LocalDateTime;

public record NotificationResponse(
        // 발신자
        String fromNickname,
        // 해당 객체 id
        Long targetId,
        // 알람 타입
        String notificationType,
        // 생성날짜
        LocalDateTime registeredAt
) {
    public static NotificationResponse fromNotificationDto(NotificationDto notificationDto) {
        return new NotificationResponse(
                notificationDto.fromNickname(),
                notificationDto.notificationArgs().getTargetId(),
                notificationDto.notificationType(),
                notificationDto.registeredAt()
        );
    }
}
