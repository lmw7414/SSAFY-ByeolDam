package com.ssafy.star.article.dto;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.image.dto.Image;
import com.ssafy.star.user.dto.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public record Article(
        Long id,
        String title,
        long hits,
        // TODO : 설명 없애고 제목만 남기기
        String description,
        DisclosureType disclosure,
        Set<String> articleHashtags,
        Constellation constellation,
        User user,
        List<CommentDto> commentList,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt,
        Image image

) {

    public static Article fromEntity(ArticleEntity entity) {

        Set<String> hashtags = new HashSet<>();
                try{
                    hashtags = entity.getArticleHashtagRelationEntities()
                .stream()
                .map(ArticleHashtagRelationEntity::getArticleHashtagEntity)
                .map(ArticleHashtagEntity::getTagName)
                .collect(Collectors.toSet());
                } catch(NullPointerException e) {
                    hashtags = null;
                }

        List<CommentDto> comments = entity.getCommentEntities()
                .stream()
                .map(CommentDto::from)
                .collect(Collectors.toList());

        Constellation constellation1 = null;
        try{
            constellation1 = Constellation.fromEntity(entity.getConstellationEntity());
        } catch(NullPointerException e) {
            constellation1 = null;
        }

        return new Article(
                entity.getId(),
                entity.getTitle(),
                entity.getHits(),
                entity.getDescription(),
                entity.getDisclosure(),
                hashtags,
                constellation1,
                User.fromEntity(entity.getOwnerEntity()),
                comments,
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt(),
                Image.fromEntity(entity.getImageEntity())
        );
    }
}
