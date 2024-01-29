package com.ssafy.star.article.dto;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.domain.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Article {

    private Long id;
    private String title;
    private String tag;
    private String nickname;
    //    private Long constellationId;
//    private imageResponse image;
    private long hits;
    private String description;
    private DisclosureType disclosure;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;


    public static Article fromEntity(ArticleEntity articleEntity) {
        return new Article(
                articleEntity.getId(),
                articleEntity.getTitle(),
                articleEntity.getTag(),
                articleEntity.getNickname(),
//                articleEntity.getConstellationId(),
//                Image.fromEntity(articleEntity.getImage()),
                articleEntity.getHits(),
                articleEntity.getDescription(),
                articleEntity.getDisclosure(),
                articleEntity.getCreatedAt(),
                articleEntity.getModifiedAt(),
                articleEntity.getDeletedAt()
        );
    }
}
