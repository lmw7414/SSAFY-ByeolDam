package com.ssafy.star.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalStatus {
    REQUEST("팔로잉 요청 중입니다."),
    ACCEPT("팔로우 중입니다."),
    CANCEL("팔로우를 취소했습니다."),
    NOTHING("팔로잉 상태가 아닙니다.");

    final String message;
}
