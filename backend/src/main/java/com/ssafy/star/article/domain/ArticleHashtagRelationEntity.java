package com.ssafy.star.article.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_tag_relation", indexes = @Index(name = "idx_saved_at", columnList = "saved_at"))
@Getter
@IdClass(ArticleHashtagPK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE article_tag_relation SET deleted_at = NOW() where article_id=?")
public class ArticleHashtagRelationEntity {

    @Id
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private ArticleEntity articleEntity;

    @Id
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_hashtag_id")
    private ArticleHashtagEntity articleHashtagEntity;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    void createdAt() {
        this.savedAt = LocalDateTime.from(LocalDateTime.now());
    }
    
    @PreUpdate
    void updatedAt() {
        this.savedAt = LocalDateTime.from(LocalDateTime.now());
    }

    private ArticleHashtagRelationEntity(ArticleHashtagEntity articleHashtagEntity, ArticleEntity articleEntity) {
        this.articleEntity = articleEntity;
        this.articleHashtagEntity = articleHashtagEntity;
    }

    public void undoDeletion(){
        this.deletedAt = null;
    }
    public static ArticleHashtagRelationEntity of(ArticleHashtagEntity articleHashtagEntity, ArticleEntity articleEntity) {
        ArticleHashtagRelationEntity articleHashtagRelationEntity = new ArticleHashtagRelationEntity(articleHashtagEntity, articleEntity);
        return articleHashtagRelationEntity;
    }

    public void updateArticleEntity(ArticleEntity articleEntity) {
        this.articleEntity = articleEntity;
    }
}
