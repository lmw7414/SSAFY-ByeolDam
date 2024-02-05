package com.ssafy.star.article.dto;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class Article {

    private Long id;
    private String title;
    private String tag;
    //    private Long constellationId;
//    private imageResponse image;
    private long hits;
    private String description;
    private DisclosureType disclosure;
    private User user;
    private List<CommentEntity> commentEntities;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;


    public static Article fromEntity(ArticleEntity entity) {
        return new Article(
                entity.getId(),
                entity.getTitle(),
                entity.getTag(),
//                entity.getConstellationId(),
//                Image.fromEntity(entity.getImage()),
                entity.getHits(),
                entity.getDescription(),
                entity.getDisclosure(),
                User.fromEntity(entity.getUser()),
                entity.getCommentEntities(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt()
        );
    }
}
