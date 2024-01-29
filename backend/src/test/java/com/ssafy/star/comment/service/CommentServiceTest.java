package com.ssafy.star.comment.service;

import com.ssafy.star.article.ArticleEntity;
import com.ssafy.star.article.ArticleRepository;
import com.ssafy.star.comment.application.CommentService;
import com.ssafy.star.comment.dao.CommentRepository;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.exception.ErrorCode;
import com.ssafy.star.comment.exception.StarApplicationException;
import com.ssafy.star.comment.fixture.ArticleEntityFixture;
import com.ssafy.star.comment.fixture.CommentEntityFixture;
import com.ssafy.star.comment.fixture.UserEntityFixture;
import com.ssafy.star.user.UserEntity;
import com.ssafy.star.user.UserRepository;
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
public class CommentServiceTest {

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
        ArticleEntity articleEntity = ArticleEntityFixture.get(articleId, null);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(articleEntity));
        when(commentRepository.findAllByArticleEntity(articleEntity, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> commentService.search(articleId, pageable));
    }

    @Test
    void 댓글조회_게시물없는경우() {
        Long articleId = 1L;
        Pageable pageable = mock(Pageable.class);
        ArticleEntity articleEntity = ArticleEntityFixture.get(articleId, null);

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
        when(commentRepository.findAllByArticleEntity(articleEntity, pageable)).thenReturn(Page.empty());

        StarApplicationException result = Assertions.assertThrows(StarApplicationException.class, () -> commentService.search(articleId, pageable));
        Assertions.assertEquals(ErrorCode.ARTICLE_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글생성_성공() {
        Long articleId = 1L;
        String userName = "userName";
        String content = "content";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(ArticleEntity.class)));
        when(commentRepository.save(any())).thenReturn(mock(CommentEntity.class));

        Assertions.assertDoesNotThrow(() -> commentService.create(articleId, userName, content, null));
    }

    @Test
    void 댓글생성_유저없는경우() {
        Long articleId = 1L;
        String userName = "userName";
        String content = "content";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(ArticleEntity.class)));
        when(commentRepository.save(any())).thenReturn(mock(CommentEntity.class));

        StarApplicationException result = Assertions.assertThrows(StarApplicationException.class, () -> commentService.create(articleId, userName, content, null));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글생성_게시물없는경우() {
        Long articleId = 1L;
        String userName = "userName";
        String content = "content";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
        when(commentRepository.save(any())).thenReturn(mock(CommentEntity.class));

        StarApplicationException result = Assertions.assertThrows(StarApplicationException.class, () -> commentService.create(articleId, userName, content, null));
        Assertions.assertEquals(ErrorCode.ARTICLE_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글수정_성공() {
        Long commentId = 1L;
        String userName = "userName";
        String content = "content";

        UserEntity mockUserEntity = UserEntityFixture.get(1L, userName);

        CommentEntity commentEntity = CommentEntityFixture.get(commentId, content, mockUserEntity, null);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mockUserEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(commentRepository.saveAndFlush(any())).thenReturn(commentEntity);

        Assertions.assertDoesNotThrow(() -> commentService.modify(commentId, userName, content));
    }

    @Test
    void 댓글수정_댓글없는경우() {
        Long commentId = 1L;
        String userName = "userName";
        String content = "content";

        UserEntity mockUserEntity = UserEntityFixture.get(1L, userName);

        CommentEntity commentEntity = CommentEntityFixture.get(commentId, content, mockUserEntity, null);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mockUserEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
        when(commentRepository.saveAndFlush(any())).thenReturn(commentEntity);

        StarApplicationException result = Assertions.assertThrows(StarApplicationException.class, () -> commentService.modify(commentId, userName, content));
        Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글수정_작성자가아닌경우() {
        Long commentId = 1L;
        String userName = "userName";
        String content = "content";

        UserEntity mockUserEntity = UserEntityFixture.get(1L, userName);

        CommentEntity commentEntity = CommentEntityFixture.get(commentId, content, mockUserEntity, null);
        UserEntity userEntity2 = UserEntityFixture.get(2L, "user2");

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity2));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mock(CommentEntity.class)));
        when(commentRepository.saveAndFlush(any())).thenReturn(commentEntity);

        StarApplicationException result = Assertions.assertThrows(StarApplicationException.class, () -> commentService.modify(commentId, userName, content));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, result.getErrorCode());
    }

    @Test
    void 댓글삭제_성공() {
        Long commentId = 1L;
        String userName = "userName";

        UserEntity mockUserEntity = UserEntityFixture.get(1L, userName);

        CommentEntity commentEntity = CommentEntityFixture.get(commentId, null, mockUserEntity, null);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mockUserEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));

        Assertions.assertDoesNotThrow(() -> commentService.delete(commentId, userName));
    }

    @Test
    void 댓글삭제_댓글없는경우() {
        Long commentId = 1L;
        String userName = "userName";

        UserEntity mockUserEntity = UserEntityFixture.get(1L, userName);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mockUserEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        StarApplicationException result = Assertions.assertThrows(StarApplicationException.class, () -> commentService.delete(commentId, userName));
        Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void 댓글삭제_작성자가아닌경우() {
        Long commentId = 1L;
        String userName = "userName";

        UserEntity mockUserEntity = UserEntityFixture.get(1L, userName);
        UserEntity userEntity2 = UserEntityFixture.get(2L, "user2");
        ArticleEntity articleEntity = ArticleEntityFixture.get(1L, mockUserEntity);
        CommentEntity commentEntity = CommentEntityFixture.get(commentId, null, mockUserEntity, articleEntity);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity2));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));

        StarApplicationException result = Assertions.assertThrows(StarApplicationException.class, () -> commentService.delete(commentId, userName));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, result.getErrorCode());
    }
}