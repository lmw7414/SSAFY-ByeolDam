package com.ssafy.star.article.dto.request;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.image.ImageType;

import java.util.Set;

public record ArticleCreateRequest (

    String title,
    String description,
    DisclosureType disclosureType,
    ImageType imageType,
    Set<String> articleHashtagSet
){}