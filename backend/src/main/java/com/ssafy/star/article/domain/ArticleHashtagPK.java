package com.ssafy.star.article.domain;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
public class ArticleHashtagPK implements Serializable {

    private ArticleEntity articleEntity;

    private ArticleHashtagEntity articleHashtagEntity;
}
