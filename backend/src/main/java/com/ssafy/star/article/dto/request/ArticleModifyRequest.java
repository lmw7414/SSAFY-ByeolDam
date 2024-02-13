package com.ssafy.star.article.dto.request;

import com.ssafy.star.common.types.DisclosureType;

import java.util.Set;


public record ArticleModifyRequest (
    String title,
    String description,
    DisclosureType disclosureType,
    Set<String> articleHashtagSet

) {
}