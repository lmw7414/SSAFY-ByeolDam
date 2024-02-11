package com.ssafy.star.article.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article_hashtag")
@Getter
@NoArgsConstructor
public class ArticleHashtagEntity {
    @Id
    @Column(name="tag_name" , nullable = false)
    private String tagName;

    @ToString.Exclude
    @OneToMany(mappedBy = "articleHashtagEntity", cascade = CascadeType.ALL)
    private List<ArticleHashtagRelationEntity> articleHashtagRelationEntities = new ArrayList<>();

    @Builder
    public ArticleHashtagEntity(String tagName) {
        this.tagName = tagName;
    }
}
