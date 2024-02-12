package com.ssafy.star.article.dto.request;

import java.util.Set;

public record ArticleConstellationSelect (
        Set<Long> articleIdSet
){
}
