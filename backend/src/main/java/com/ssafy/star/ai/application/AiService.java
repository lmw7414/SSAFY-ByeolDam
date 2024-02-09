package com.ssafy.star.ai.application;

import com.ssafy.star.common.infra.S3.S3uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AiService {

    private final S3uploader s3uploader;
    private static final int TARGET_HEIGHT = 1024;

    @Transactional
    public String uploadTempImage(MultipartFile imageFile){

        String imageUrl = "";
        try{
            imageUrl = s3uploader.upload(imageFile, "temp", TARGET_HEIGHT);
            return imageUrl;
        } catch (IOException e){
            System.out.println("업로드 실패");
        }
        return imageUrl;
    }

}
