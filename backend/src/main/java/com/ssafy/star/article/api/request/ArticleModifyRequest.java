package com.ssafy.star.article.api.request;

import com.ssafy.star.article.DisclosureType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleModifyRequest {
    private String title;
    private String tag;
    private String description;
    private DisclosureType disclosureType;
}