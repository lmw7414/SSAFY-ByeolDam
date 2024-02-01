package com.ssafy.star.article.dto.request;

import com.ssafy.star.article.DisclosureType;
import lombok.AllArgsConstructor;
import lombok.Getter;


public record ArticleModifyRequest (
    String title,
    String tag,
    String description,
    DisclosureType disclosureType)
{
}