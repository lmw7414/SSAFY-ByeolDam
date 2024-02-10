package com.ssafy.star.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationArgs {
    // user who occur alarm
    private Long fromUserId;
    private Long targetId;
}
