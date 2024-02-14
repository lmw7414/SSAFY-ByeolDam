package com.ssafy.star.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    NEW_COMMENT_ON_POST("new comment"),
    NEW_COMMENT_ON_COMMENT("new children comment"),
    NEW_LIKE_ON_POST("new like"),
    FOLLOW_REQUEST("follow request"),
    FOLLOW_ACCEPT("follow accept"),
    FOLLOWED("followed")
    ;

    private final String alarmText;
}