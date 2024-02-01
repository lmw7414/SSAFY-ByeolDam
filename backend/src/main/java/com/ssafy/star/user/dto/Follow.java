package com.ssafy.star.user.dto;

import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.domain.FollowEntity;

import java.time.LocalDateTime;

public record Follow(
        Long id,
        User fromUser,
        User toUser,
        LocalDateTime requestDate,
        LocalDateTime acceptDate,
        ApprovalStatus status

) {
    public static Follow of(Long id, User fromUser, User toUser, LocalDateTime requestDate, LocalDateTime acceptDate, ApprovalStatus status) {
        return new Follow(
                id,
                fromUser,
                toUser,
                requestDate,
                acceptDate,
                status
        );
    }
    public static Follow fromEntity(FollowEntity entity) {
        return new Follow(
                entity.getId(),
                User.fromEntity(entity.getFromUser()),
                User.fromEntity(entity.getToUser()),
                entity.getRequestDate(),
                entity.getAcceptDate(),
                entity.getStatus()
        );
    }
}
