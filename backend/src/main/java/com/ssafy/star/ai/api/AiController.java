package com.ssafy.star.ai.api;


import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.star.ai.application.AiService;
import com.ssafy.star.article.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping
    public Response<JsonNode> uploadImage(@RequestParam MultipartFile imageFile) throws IOException {

        return aiService.connectDjango(imageFile);
    }

}
