    package com.ssafy.star.comment.controller;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.ssafy.star.comment.application.CommentService;
    import com.ssafy.star.comment.dto.request.CommentCreateRequest;
    import com.ssafy.star.comment.dto.request.CommentModifyRequest;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.boot.test.mock.mockito.MockBean;
    import org.springframework.data.domain.Page;
    import org.springframework.http.MediaType;
    import org.springframework.security.test.context.support.WithMockUser;
    import org.springframework.test.web.servlet.MockMvc;

    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.ArgumentMatchers.anyLong;
    import static org.mockito.Mockito.when;
    import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @SpringBootTest
    @AutoConfigureMockMvc
    class CommentControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private CommentService commentService;

        @Test
        @WithMockUser
        void 댓글조회() throws Exception {
            Long articleId = 1L;
            when(commentService.search(anyLong(), any())).thenReturn(Page.empty());

            mockMvc.perform(get("/api/v1/comments/{articleId}/comments", articleId)
                            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void 댓글작성() throws Exception {
            Long articleId = 1L;
            String content = "content";

            mockMvc.perform(post("/api/v1/comments/articles/{articleId}/comments", articleId)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new CommentCreateRequest(articleId, content, null)))
            ).andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void 댓글수정() throws Exception {
            Long commentId = 1L;
            String content = "content";

            mockMvc.perform(put("/api/v1/comments/articles/{articleId}/comments/{commentId}", 1L, commentId)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(new CommentModifyRequest(commentId, content)))
                    ).andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void 댓글삭제() throws Exception {
            Long commentId = 1L;

            mockMvc.perform(delete("/api/v1/comments/comments/{commentId}", commentId)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(commentId))
                    ).andDo(print())
                    .andExpect(status().isOk());
        }
    }
