package com.ssafy.star.image.dao;

import com.ssafy.star.image.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    String defaultProfileUrl = "https://byeoldam.s3.ap-northeast-2.amazonaws.com/profiles/defaultProfile.png";

    Optional<ImageEntity> findByUrl(String defaultProfileUrl);

}
