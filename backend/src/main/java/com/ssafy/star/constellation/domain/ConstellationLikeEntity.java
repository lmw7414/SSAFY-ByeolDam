package com.ssafy.star.constellation.domain;

import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "constellationLike")
@Getter
@Setter
public class ConstellationLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "constellationId", nullable = false)
    private ConstellationEntity constellationEntity;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void createdAt() { this.createdAt = LocalDateTime.from(LocalDateTime.now()); }

    protected ConstellationLikeEntity() {}

    private ConstellationLikeEntity(UserEntity userEntity, ConstellationEntity constellationEntity) {
        this.userEntity = userEntity;
        this.constellationEntity = constellationEntity;
    }

    public static ConstellationLikeEntity of(UserEntity userEntity, ConstellationEntity constellationEntity) {
        return new ConstellationLikeEntity(userEntity, constellationEntity);
    }
}