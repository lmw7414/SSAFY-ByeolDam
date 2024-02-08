package com.ssafy.star.ai.api;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.star.ai.application.AiService;
import com.ssafy.star.ai.dto.AiResponse;
import com.ssafy.star.article.dto.response.Response;
import com.ssafy.star.common.infra.S3.S3uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final RestTemplate restTemplate;
    private final S3uploader s3uploader;

    @PostMapping
    public Response<JsonNode> uploadImage(@RequestParam MultipartFile imageFile) throws IOException {

        String fileName = aiService.uploadTempImage(imageFile);

        System.out.println("fileName : " + fileName);

        // Django 서버 URL
        String djangoUrl = "http://192.168.219.101:8000/ByeolDamAI/getImage/";

        // HTTP 요청을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 파일 이름을 JSON 형태로 변환하여 HTTP 요청 보내기
        String jsonBody = "{\"url\": \"" + fileName + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        System.out.println("jsonBody : " + jsonBody);

        System.out.println("requestEntity : "+requestEntity);
        // Django 서버로 HTTP POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(djangoUrl, requestEntity, String.class);

//        System.out.println("responseEntity : "+responseEntity);
//        System.out.println(responseEntity.getBody().replace("\\",""));

        // S3 파일 삭제
        s3uploader.deleteImageFromS3(fileName);

        String jsonString = responseEntity.getBody();

        System.out.println("jsonString: " + jsonString);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        System.out.println("jsonNode : " + jsonNode);

        return Response.success(jsonNode);
    }

}
