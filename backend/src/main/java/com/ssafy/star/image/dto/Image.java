package com.ssafy.star.image.dto;

import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.domain.ImageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Image {

    private Long id;
    private String name;
    private String url;
    private String thumbnailUrl;
    private ImageType imageType;

    public static Image fromEntity(ImageEntity imageEntity){
        return new Image(
                imageEntity.getId(),
                imageEntity.getName(),
                imageEntity.getUrl(),
                imageEntity.getThumbnailUrl(),
                imageEntity.getImageType()
        );
    }

    public ImageEntity toEntity(){
        return ImageEntity.of(
                this.id,
                this.name,
                this.url,
                this.thumbnailUrl,
                this.imageType
        );
    }

}
