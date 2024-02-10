package com.ssafy.star.article.dto.response;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.comment.dto.response.CommentResponse;
import com.ssafy.star.image.dto.response.ImageResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record ArticleResponse (
        Long id,
        String title,
        long hits,
        String description,
        DisclosureType disclosure,
        Set<String> articleHashtags,
        String constellationEntityName,
        String ownerEntityNickname,
        List<CommentResponse> commentResponse,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt,
        ImageResponse image
){

    public static ArticleResponse fromArticle(Article article) {
        return new ArticleResponse(
                article.id(),
                article.title(),
                article.hits(),
                article.description(),
                article.disclosure(),
                article.articleHashtags(),
                article.constellationEntityName(),
                article.ownerEntityNickname(),
                article.commentList().stream().map(CommentResponse::fromComment).toList(),
                article.createdAt(),
                article.modifiedAt(),
                article.deletedAt(),
                ImageResponse.fromImage(article.image())
        );
    }
}