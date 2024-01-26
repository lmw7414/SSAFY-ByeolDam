package com.ssafy.star.common.infra.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.ssafy.star.common.utils.ImageUtils.convert;
import static com.ssafy.star.common.utils.ImageUtils.resizeImage;

@Slf4j
@RequiredArgsConstructor
public class S3uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {

        BufferedImage resizedImage = resizeImage(multipartFile);
        File uploadFile = convert(resizedImage, multipartFile.getOriginalFilename());


        String uuid = UUID.randomUUID().toString();
        String extension = uploadFile.getName().substring(uploadFile.getName().lastIndexOf(".")+1);
        if(extension.equals("blob")) extension = "png";
        String fileName = dirName + "/" + uuid + "." + extension;
//        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()){
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 않았습니다.");
        }
    }

    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(
            new PutObjectRequest(bucket, fileName, uploadFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

}
