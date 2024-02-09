package com.ssafy.star.article.dto;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.image.dto.Image;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public record Article (
        Long id,
    String title,
//    private imageResponse image;
    long hits,

    String description,
    DisclosureType disclosure,
    Set<String> articleHashtags,
    String constellationEntityName,
    String ownerEntityNickname,
    List<CommentDto> commentList,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    Image image

    ) {
    public static Article fromEntity(ArticleEntity entity){
        return new Article(
                entity.getId(),
                entity.getTitle(),
                entity.getHits(),
                entity.getDescription(),
                entity.getDisclosure(),
                (Set<String>) entity.getArticleHashtagRelationEntities()
                        .stream()
                        .map(ArticleHashtagRelationEntity::getArticleHashtagEntity)
                        .map(ArticleHashtagEntity::getTagName)
                        .collect(Collectors.toSet()),
                entity.getConstellationEntity() != null ? entity.getConstellationEntity().getName() : null,
                entity.getOwnerEntity().getNickname(),
                entity.getCommentEntities().stream().map(CommentDto::from).toList(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                Image.fromEntity(entity.getImageEntity())
        );
    }
}
