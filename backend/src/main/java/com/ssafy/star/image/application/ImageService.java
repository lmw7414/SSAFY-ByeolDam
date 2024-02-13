package com.ssafy.star.image.application;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.dao.ImageRepository;
import com.ssafy.star.image.domain.ImageEntity;
import com.ssafy.star.image.dto.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public ImageEntity saveImage(String name, String url, String thumbnailUrl, ImageType imageType){
        return imageRepository.save(ImageEntity.of(name, url, thumbnailUrl, imageType));
    }

    @Transactional
    public void saveProfileImage(String name, String url, ImageType imageType){
        imageRepository.save(ImageEntity.of(name, url, imageType));
    }

    @Transactional
    public Image getImageUrl(String url){
        ImageEntity imageEntity = imageRepository.findByUrl(url).orElseThrow(()-> new ByeolDamException(ErrorCode.IMAGE_NOT_FOUND_ERROR, String.format("%s not founded", url)));
        return Image.fromEntity(imageEntity);
    }

    @Transactional
    public void deleteImage(Image image){
        imageRepository.deleteById(image.id());
    }

}
