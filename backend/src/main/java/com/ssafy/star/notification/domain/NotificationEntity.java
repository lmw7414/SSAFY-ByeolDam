package com.ssafy.star.notification.domain;

import com.ssafy.star.notification.dto.NotificationArgs;
import com.ssafy.star.notification.dto.NotificationType;
import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "\"notification\"", indexes = {
        @Index(name = "user_id", columnList = "user_id")
})
@SQLDelete(sql = "UPDATE 'notification' SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(nullable = false)
    private String fromNickname;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(nullable = false)
    @Embedded
    public NotificationArgs notificationArgs;

    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @PrePersist
    void registeredAt() { this.registeredAt = LocalDateTime.from(LocalDateTime.now()); }

    @PreUpdate
    void updatedAt() { this.updatedAt = LocalDateTime.from(LocalDateTime.now()); }

    protected NotificationEntity() {}

    private NotificationEntity(NotificationType notificationType, NotificationArgs notificationArgs, UserEntity userEntity, String fromNickname) {
        this.notificationType = notificationType;
        this.notificationArgs = notificationArgs;
        this.userEntity = userEntity;
        this.fromNickname = fromNickname;
    }

    public static NotificationEntity of(NotificationType notificationType, NotificationArgs notificationArgs, UserEntity userEntity, String fromNickname) {
        return new NotificationEntity(notificationType, notificationArgs, userEntity, fromNickname);
    }
}
