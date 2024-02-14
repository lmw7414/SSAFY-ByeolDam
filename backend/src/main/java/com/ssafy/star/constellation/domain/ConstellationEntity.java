package com.ssafy.star.constellation.domain;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "constellation")
@ToString
@Getter
public class ConstellationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    @Column(name = "contour_id")
    private Long contourId;

    private Long hits = 0L;

    @ToString.Exclude
    @OneToMany(mappedBy = "constellationEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ConstellationUserEntity> constellationUserEntities = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "constellationEntity")
    private List<ArticleEntity> articleEntities;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.from(LocalDateTime.now());
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = LocalDateTime.from(LocalDateTime.now());
    }

    public void increaseHits() {
        this.hits++;
    }

    // TODO : 별자리 Admin, user 나누는 로직 확인좀

    public void addUser(ConstellationUserEntity constellationUserEntity) {
        this.constellationUserEntities.add(constellationUserEntity);
        constellationUserEntity.updateConstellationEntity(this);
    }

    public void deleteUser(ConstellationUserEntity constellationUserEntity) {
        this.constellationUserEntities.remove(constellationUserEntity);
        constellationUserEntity.updateConstellationEntity(this);
    }

    public UserEntity getAdminEntity() {
        for (int i = 0; i < constellationUserEntities.size(); i++) {
            if(constellationUserEntities.get(i).getConstellationUserRole().equals(ConstellationUserRole.ADMIN)) {
                return constellationUserEntities.get(i).getUserEntity();
            }
        }
        return null;
    }

    protected ConstellationEntity() {
    }

    private ConstellationEntity(String name) {
        this.name = name;
    }

    public static ConstellationEntity of(String name) {
        return new ConstellationEntity(name);
    }
}
