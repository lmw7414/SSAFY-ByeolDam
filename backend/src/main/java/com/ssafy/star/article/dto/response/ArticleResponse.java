package com.ssafy.star.article.dto.response;

import com.ssafy.star.article.dto.Article;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.common.types.DisclosureType;
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
        List<CommentDto> commentList,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt,
        ImageResponse imageResponse
){

    public static ArticleResponse fromArticle(Article article) {

        return new ArticleResponse(
                article.id(),
                article.title(),
                article.hits(),
                article.description(),
                article.disclosure(),
                article.articleHashtags(),
                article.constellation().name(),
                article.user().nickname(),
                article.commentList(),
                article.createdAt(),
                article.modifiedAt(),
                article.deletedAt(),
                ImageResponse.fromImage(article.image())
        );
    }
}