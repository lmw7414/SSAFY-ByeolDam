package com.ssafy.star.user.dto.request;

import com.ssafy.star.user.domain.ApprovalStatus;

public record FollowAcceptRequest(
        String toUserNickname,
        ApprovalStatus status
) {
}
