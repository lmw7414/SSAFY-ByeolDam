package com.ssafy.star.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    // 어떤 이벤트인지
    private NotificationType type;
    // 누가 누가에게
    private NotificationArgs args;
    private Long receiverUserId;
}
