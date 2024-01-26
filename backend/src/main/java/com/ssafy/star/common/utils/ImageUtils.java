package com.ssafy.star.common.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    private static final int TARGET_HEIGHT = 1024;

    //이미지 리사이징
    public static BufferedImage resizeImage(MultipartFile multipartFile) throws IOException{

        //1. MultipartFile에서 BufferedImage로 변환
//        BufferedImage sourceImage = ImageIO.read(multipartFile.getInputStream());
        BufferedImage sourceImage = turnImage(multipartFile);

        //만약 크기가 TARGET_HEIGHT인 1024보다 작거나 같다면 sourceImage를 그대로 반환한다.
        if(sourceImage.getHeight() <= TARGET_HEIGHT){
            return sourceImage;
        }

        double sourceImageRatio = (double) sourceImage.getWidth()/ sourceImage.getHeight();

        int resizedWidth = (int)(TARGET_HEIGHT * sourceImageRatio);

        return Scalr.resize(sourceImage, resizedWidth, TARGET_HEIGHT);
    }

    //썸네일 리사이징
    public static BufferedImage resizeThumbnail(MultipartFile multipartFile) throws IOException{

        //1. MultipartFile에서 BufferedImage로 변환
//        BufferedImage sourceThumbnail = ImageIO.read(multipartFile.getInputStream());
        BufferedImage sourceThumbnail = turnImage(multipartFile);

        int width = sourceThumbnail.getWidth();
        int height = sourceThumbnail.getHeight();
        int minLen = Math.min(width, height);

        BufferedImage thumbImage = Scalr.crop(sourceThumbnail, (width-minLen)/2, (height-minLen)/2, minLen, minLen);

        return Scalr.resize(thumbImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 200);
    }

    //BufferedImage를 File로 변환
    public static File convert(BufferedImage bufferedImage, String sourceFileName) throws IOException{
        try{
            File file = new File(sourceFileName);
            String extension = sourceFileName.substring(sourceFileName.lastIndexOf(".")+1);
            if(extension.equals("blob")) extension="png";
            ImageIO.write(bufferedImage, extension, file);
            return file;
        }catch (IOException e){
            e.printStackTrace();
        }
        throw new IllegalStateException("Cannot convert BufferedImage to File");
    }


    //사진을 회전시키는 함수
    private static BufferedImage turnImage(MultipartFile multipartFile) throws IOException{
        //원본 파일의 Orientation 정보를 읽는다.
        int orientation = 1;    // 회전정보, 1. 0도 3. 180도 6. 270도 8. 90도 회전한 정보

        Metadata metadata;  //이미지 메타 데이터 객체
        Directory directory;
        JpegDirectory jpegDirectory;

        File imageFile = new File(multipartFile.getOriginalFilename());
        multipartFile.transferTo(imageFile);

        try{
            metadata = ImageMetadataReader.readMetadata(imageFile);
            directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
            if(directory != null){
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);  //회전 정보
            }
        }catch (Exception e){
            orientation = 1;
        }

        //imageFile
        BufferedImage sourceImage = ImageIO.read(imageFile);

        //회전시키기
        switch (orientation){
            case 1:
                break;
            case 3:
                sourceImage = Scalr.rotate(sourceImage, Scalr.Rotation.CW_180, null);
                break;
            case 6:
                sourceImage = Scalr.rotate(sourceImage, Scalr.Rotation.CW_90, null);
            case 8:
                sourceImage = Scalr.rotate(sourceImage, Scalr.Rotation.CW_270, null);
            default:
                orientation = 1;
                break;
        }
        return sourceImage;
    }


}
