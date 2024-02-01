package com.ssafy.star.article.dto.request;

import com.ssafy.star.article.DisclosureType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public record ArticleCreateRequest (

    String title,
    String tag,
   String description,
    DisclosureType disclosureType
//    private image;
){
}