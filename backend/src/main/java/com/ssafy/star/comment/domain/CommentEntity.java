package com.ssafy.star.comment.domain;

import com.ssafy.star.article.ArticleEntity;
import com.ssafy.star.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "comment", indexes = {
        @Index(name = "article_id_idx", columnList = "articleEntity")
})
@Getter
@Setter
public class CommentEntity {
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private ArticleEntity articleEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(updatable = false)
    private Long parentCommentId;

    @ToString.Exclude
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private Set<CommentEntity> childrenComments = new LinkedHashSet<>();

    @PrePersist
    void createdAt() { this.createdAt = LocalDateTime.from(LocalDateTime.now()); }

    @PreUpdate
    void modifiedAt() { this.modifiedAt = LocalDateTime.from(LocalDateTime.now()); }

    protected CommentEntity() {}

    private CommentEntity(UserEntity userEntity, ArticleEntity articleEntity, String content, Long parentCommentId) {
        this.userEntity = userEntity;
        this.articleEntity = articleEntity;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public static CommentEntity of(UserEntity userEntity, ArticleEntity articleEntity, String content, Long parentCommentId) {
        return new CommentEntity(userEntity, articleEntity, content, parentCommentId);
    }
}

