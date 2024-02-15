package com.ssafy.star.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.global.oauth.domain.ProviderType;
import com.ssafy.star.image.domain.ImageEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Table(name = "\"user_account\"")
@SQLDelete(sql = "UPDATE user_account SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class UserEntity {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "provider_type", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderType providerType;

    @Column(name = "role_type", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @JsonIgnore
    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false, length = 50)
    private String name;

    @Setter
    @Column(nullable = false, length = 32, unique = true)
    private String nickname;

    @Setter
    @Column(length = 512)
    private String memo;

    @Setter
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DisclosureType disclosureType = DisclosureType.VISIBLE;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "fromUser", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<FollowEntity> followEntities = new ArrayList<>();
//
//    @ToString.Exclude
//    @OneToMany(mappedBy = "userEntity", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ConstellationUserEntity> constellationUserEntities = new ArrayList<>();
//
//    @ToString.Exclude
//    @OneToMany(mappedBy = "ownerEntity", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ArticleEntity> articleEntities = new ArrayList<>();

    @Setter
    private LocalDate birthday;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    private ImageEntity imageEntity;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.from(LocalDateTime.now());
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = LocalDateTime.from(LocalDateTime.now());
    }

    protected UserEntity() {
    }

    private UserEntity(
            String email,
            ProviderType providerType,
            RoleType roleType,
            String password,
            String name,
            String nickname,
            String memo,
            DisclosureType disclosureType,
            LocalDate birthday,
            ImageEntity imageEntity
    ) {
        this.email = email;
        this.providerType = providerType;
        this.roleType = roleType;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.memo = memo;
        this.disclosureType = disclosureType;
        this.birthday = birthday;
        this.imageEntity = imageEntity;
    }

    public static UserEntity of(String email, ProviderType providerType, String password, String name, String nickname) {
        return of(email, providerType, RoleType.USER, password, name, nickname, null, DisclosureType.VISIBLE, null, null);
    }

    public static UserEntity of(String email, ProviderType providerType, String password, String name, String nickname, ImageEntity imageEntity) {
        return of(email, providerType, RoleType.USER, password, name, nickname, null, DisclosureType.VISIBLE, null, imageEntity);
    }

    public static UserEntity of(String email, ProviderType providerType, RoleType roleType, String password, String name, String nickname, String memo, DisclosureType disclosureType, LocalDate birthday, ImageEntity imageEntity) {
        return new UserEntity(email, providerType, roleType, password, name, nickname, memo, disclosureType, birthday, imageEntity);
    }

}
