package com.ssafy.star.article.dto.response;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.dto.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String tag;
//    private Long constellationId;
//    private imageResponse image;
    private DisclosureType disclosure;
    private long hits;
    private String description;
    private List<CommentEntity> commentEntities;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    public static ArticleResponse fromArticle(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getTag(),
//                article.getConstellationId(),
//                ImageResponse.fromImage(Article.getImage()),
                article.getDisclosure(),
                article.getHits(),
                article.getDescription(),
                article.getCommentEntities(),
                article.getCreatedAt(),
                article.getModifiedAt(),
                article.getDeletedAt()
        );
    }
}