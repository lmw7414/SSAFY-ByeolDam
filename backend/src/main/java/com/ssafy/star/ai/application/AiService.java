package com.ssafy.star.ai.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.star.common.infra.S3.S3uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ssafy.star.article.dto.response.Response;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AiService {

    private final S3uploader s3uploader;
    private final RestTemplate restTemplate;

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

    @Transactional
    public Response<JsonNode> connectDjango(MultipartFile imageFile) throws JsonProcessingException {
        String fileName = uploadTempImage(imageFile);
        // Django 서버 URL
        String djangoUrl = "http://125.241.47.137:8000/ByeolDamAI/getImage/";

        // HTTP 요청을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 파일 이름을 JSON 형태로 변환하여 HTTP 요청 보내기
        String jsonBody = "{\"url\": \"" + fileName + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        // Django 서버로 HTTP POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(djangoUrl, requestEntity, String.class);

        // S3 파일 삭제
        s3uploader.deleteImageFromS3(fileName);

        String jsonString = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        return Response.success(jsonNode);
    }

}
