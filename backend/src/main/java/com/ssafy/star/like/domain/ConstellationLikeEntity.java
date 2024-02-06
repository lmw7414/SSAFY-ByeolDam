package com.ssafy.star.like.domain;

import com.ssafy.star.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    protected ConstellationLikeEntity() {}

    private ConstellationLikeEntity(UserEntity userEntity, ConstellationEntity constellationEntity) {
        this.userEntity = userEntity;
        this.constellationEntity = constellationEntity;
    }

    public static ConstellationLikeEntity of(UserEntity userEntity, ConstellationEntity constellationEntity) {
        return new ConstellationLikeEntity(userEntity, constellationEntity);
    }
}
