package com.ssafy.star.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "User email is duplicated"),
    DUPLICATED_USER_NICKNAME(HttpStatus.CONFLICT, "User nickname is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    SELF_FOLLOW_ERROR(HttpStatus.BAD_REQUEST, "Self following is invalid"),
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Article not founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not founded"),
    REPLY_TO_REPLY(HttpStatus.BAD_REQUEST, "unable to reply to parent comments"),
    INVALID_CONTENT(HttpStatus.BAD_REQUEST, "Content type is invalid"),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "Follow status not founded in follow list"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Request is invalid"),
    NOTIFICATION_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Connect to notification occurs error"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");


    final private HttpStatus status;
    final private String message;
}
