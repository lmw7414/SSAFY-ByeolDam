package com.ssafy.star.image.domain;


import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.dto.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "\"image\"")
@Getter
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Setter
    @NotNull
    @Column(name = "image_name", nullable = false, length = 255)
    private String name;

    @Setter
    @NotNull
    @Column(name = "image_url", nullable = false, length = 512)
    private String url;

    @Setter
    @Column(name = "thumbnail_url", length = 512)
    private String thumbnailUrl;

    @Setter
    @NotNull
    @Column(name = "image_type", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    protected ImageEntity() {
    }

    private ImageEntity(String name, String url, String thumbnailUrl, ImageType imageType) {
        this.name = name;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.imageType = imageType;
    }

    public static ImageEntity of(String name, String url, String thumbnailUrl, ImageType imageType) {
        return new ImageEntity(name, url, thumbnailUrl, imageType);
    }

    public static ImageEntity of(String name, String url, ImageType imageType) {
        return of(name, url, null, imageType);
    }

    public static ImageEntity fromDto(Image dto) {
        return of(
                dto.name(),
                dto.url(),
                dto.thumbnailUrl(),
                dto.imageType()
        );
    }

}
