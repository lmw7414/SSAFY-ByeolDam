package com.ssafy.star.article.domain;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.like.domain.ArticleLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"article\"")
@Getter
@Setter
@SQLDelete(sql = "UPDATE `article` SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class ArticleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 105)
    private String title;

    @Column(name = "tag", length = 255)
    private String tag;

    // TODO : ConstellationId, Image

    @Column(name = "hits", nullable = false)
    private long hits;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "disclosure", nullable = false)
    private DisclosureType disclosure;

    @OneToMany(mappedBy = "articleEntity", orphanRemoval = true)
    private List<ArticleLikeEntity> articleLikes = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    //Image(사진) Table의 FK
    //1:1관계
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "Image")
//    private ImageEntity imageEntity;

    //Constellation(별자리) Table의 FK
    //N:1관계
//    @ManyToOne
//    @JoinColumn(name = "Constellation")
//    private ConstellationEntity constellationEntity;

//    User(회원) Table의 FK
//    0~N:1관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @PrePersist
    void createdAt() { this.createdAt = LocalDateTime.from(LocalDateTime.now()); }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = LocalDateTime.from(LocalDateTime.now());
    }

    public static ArticleEntity of(String title, String tag, String description, DisclosureType disclosure, UserEntity userEntity){
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(title);
        entity.setTag(tag);
        entity.setDescription(description);
        entity.setDisclosure(disclosure);
        entity.setUser(userEntity);
        return entity;
    }

}
