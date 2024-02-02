package com.ssafy.star.comment.service;

import com.ssafy.star.article.application.fixture.ArticleEntityFixture;
import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.comment.application.CommentService;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.fixture.CommentEntityFixture;
import com.ssafy.star.comment.repository.CommentRepository;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.user.application.fixture.UserEntityFixture;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ArticleRepository articleRepository;

    @Test
    void 댓글조회_성공() {
        Long articleId = 1L;
        Pageable pageable = mock(Pageable.class);
        ArticleEntity articleEntity = ArticleEntityFixture.get(null, null, null, null, null, null, null, null);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(articleEntity));
        when(commentRepository.findAllByArticleEntity(articleEntity, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> commentService.search(articleId, pageable));
    }

    @Test
    void 댓글조회_게시물없는경우() {
        Long articleId = 1L;
        Pageable pageable = mock(Pageable.class);
        ArticleEntity articleEntity = ArticleEntityFixture.get(null, null, null, null, null, null, null, null);

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
        when(commentRepository.findAllByArticleEntity(articleEntity, pageable)).thenReturn(Page.empty());

        ByeolDamException result = Assertions.assertThrows(ByeolDamException.class, () -> commentService.search(articleId, pageable));
        Assertions.assertEquals(ErrorCode.ARTICLE_NOT_FOUND, result.getErrorCode());
    }

//    @Test
//    void 댓글생성_성공() {
//        ArticleEntity articleEntity = ArticleEntity.of(null, null, null, null, null);
//        articleEntity.setId(1L);
//        Long articleId = articleEntity.getId();
//        String email = "email";
//        String content = "content";
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(UserEntity.class)));
//        when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(ArticleEntity.class)));
//        when(commentRepository.save(any())).thenReturn(mock(CommentEntity.class));
//
//        Assertions.assertDoesNotThrow(() -> commentService.create(articleId, email, content, null));
//    }

    @Test
    void 댓글생성_유저없는경우() {
        Long articleId = 1L;
        String email = "email";
        String content = "content";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(ArticleEntity.class)));
        when(commentRepository.save(any())).thenReturn(mock(CommentEntity.class));

        ByeolDamException result = Assertions.assertThrows(ByeolDamException.class, () -> commentService.create(articleId, email, content, null));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글생성_게시물없는경우() {
        Long articleId = 1L;
        String email = "email";
        String content = "content";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
        when(commentRepository.save(any())).thenReturn(mock(CommentEntity.class));

        ByeolDamException result = Assertions.assertThrows(ByeolDamException.class, () -> commentService.create(articleId, email, content, null));
        Assertions.assertEquals(ErrorCode.ARTICLE_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글수정_성공() {
        Long commentId = 1L;
        String email = "email";
        String content = "content";

        UserEntity mockUserEntity = UserEntityFixture.get(email);
        ArticleEntity articleEntity = ArticleEntity.of(null, null, null, null, null);
        CommentEntity commentEntity = CommentEntityFixture.get(commentId, content, mockUserEntity, null);
        commentEntity.setArticleEntity(articleEntity);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUserEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(commentRepository.saveAndFlush(any())).thenReturn(commentEntity);

        Assertions.assertDoesNotThrow(() -> commentService.modify(commentId, email, content));
    }

    @Test
    void 댓글수정_댓글없는경우() {
        Long commentId = 1L;
        String email = "email";
        String content = "content";

        UserEntity mockUserEntity = UserEntityFixture.get(email);

        CommentEntity commentEntity = CommentEntityFixture.get(commentId, content, mockUserEntity, null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUserEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
        when(commentRepository.saveAndFlush(any())).thenReturn(commentEntity);

        ByeolDamException result = Assertions.assertThrows(ByeolDamException.class, () -> commentService.modify(commentId, email, content));
        Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글수정_작성자가아닌경우() {
        Long commentId = 1L;
        String email = "email";
        String content = "content";

        UserEntity mockUserEntity = UserEntityFixture.get(email);

        CommentEntity commentEntity = CommentEntityFixture.get(commentId, content, mockUserEntity, null);
        UserEntity userEntity2 = UserEntityFixture.get("user2");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity2));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mock(CommentEntity.class)));
        when(commentRepository.saveAndFlush(any())).thenReturn(commentEntity);

        ByeolDamException result = Assertions.assertThrows(ByeolDamException.class, () -> commentService.modify(commentId, email, content));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, result.getErrorCode());
    }

    @Test
    void 댓글삭제_성공() {
        Long commentId = 1L;
        String email = "email";

        UserEntity mockUserEntity = UserEntityFixture.get(email);

        CommentEntity commentEntity = CommentEntityFixture.get(commentId, null, mockUserEntity, null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUserEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));

        Assertions.assertDoesNotThrow(() -> commentService.delete(commentId, email));
    }

    @Test
    void 댓글삭제_댓글없는경우() {
        Long commentId = 1L;
        String email = "email";

        UserEntity mockUserEntity = UserEntityFixture.get(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUserEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        ByeolDamException result = Assertions.assertThrows(ByeolDamException.class, () -> commentService.delete(commentId, email));
        Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글삭제_작성자가아닌경우() {
        Long commentId = 1L;
        String email = "email";

        UserEntity mockUserEntity = UserEntityFixture.get(email);
        UserEntity userEntity2 = UserEntityFixture.get("user2");
        ArticleEntity articleEntity = ArticleEntityFixture.get(null, null, null, null, null, null, null, null);
        CommentEntity commentEntity = CommentEntityFixture.get(commentId, null, mockUserEntity, articleEntity);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity2));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));

        ByeolDamException result = Assertions.assertThrows(ByeolDamException.class, () -> commentService.delete(commentId, email));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, result.getErrorCode());
    }
}