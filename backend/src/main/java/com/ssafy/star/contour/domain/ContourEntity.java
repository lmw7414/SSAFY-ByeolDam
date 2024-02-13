package com.ssafy.star.contour.domain;

import lombok.Getter;
import lombok.Setter;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Document(collection = "contour")
public class ContourEntity {

    @Transient
    public static final String SEQUENCE_NAME = "contours_sequence";

    @Id
    private Long _id;

    private String originUrl; // 원본 사진 저장 url
    private String thumbUrl;  // 썸네일 사진 저장 url
    private String cThumbUrl; // 윤곽선이 합쳐진 썸네일 사진 url
    private List<List<List<Integer>>> contoursList; //좌표 정보들
    private List<List<Integer>> ultimate; //선택된 윤곽선 좌표 정보

    protected ContourEntity() {
    }

    private ContourEntity(
            String originUrl,
            String thumbUrl,
            String cThumbUrl,
            List<List<List<Integer>>> contoursList,
            List<List<Integer>> ultimate
            ) {
        this.originUrl = originUrl;
        this.thumbUrl = thumbUrl;
        this.cThumbUrl = cThumbUrl;
        this.contoursList = contoursList;
        this.ultimate = ultimate;
    }

    public static ContourEntity of(String originUrl,
                                   String thumbUrl,
                                   String cThumbUrl,
                                   List<List<List<Integer>>> contoursList,
                                   List<List<Integer>> ultimate) {
        return new ContourEntity(originUrl, thumbUrl, cThumbUrl, contoursList, ultimate);
    }

}
