package com.ssafy.star.notification.dto;

import com.ssafy.star.notification.domain.NotificationEntity;

import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        String fromNickname,
        String notificationType,
        NotificationArgs notificationArgs,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        LocalDateTime removedAt
) {
    public static NotificationDto fromEntity(NotificationEntity notificationentity) {
        return new NotificationDto(
                notificationentity.getId(),
                notificationentity.getFromNickname(),
                notificationentity.getNotificationType().getAlarmText(),
                notificationentity.getNotificationArgs(),
                notificationentity.getRegisteredAt(),
                notificationentity.getUpdatedAt(),
                notificationentity.getRemovedAt()
        );
    }
}
