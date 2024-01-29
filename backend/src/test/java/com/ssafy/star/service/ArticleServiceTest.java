package com.ssafy.star.service;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.dao.UserRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.UserEntity;
import com.ssafy.star.article.exception.ErrorCode;
import com.ssafy.star.article.exception.SnsApplicationException;
import com.ssafy.star.fixture.ArticleEntityFixture;
import com.ssafy.star.fixture.UserEntityFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.ssafy.star.article.DisclosureType.VISIBLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleEntityRepository;
    @MockBean
    private UserRepository userEntityRepository;

    @Test
    void 포스트작성이_성공한경우() {
        String title = "title";
        String tag = "tag";
        String description = "설명입니다";
        DisclosureType disclosureType = VISIBLE;
        String userName = "userName";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(articleEntityRepository.save(any())).thenReturn(mock(ArticleEntity.class));
        Assertions.assertDoesNotThrow(() -> articleService.create(title,tag,description,disclosureType,userName));
    }

    @Test
    void 포스트작성시_요청한유저가_존재하지않는경우() {
        String title = "title";
        String tag = "tag";
        String description = "설명입니다";
        DisclosureType disclosureType = VISIBLE;
        String userName = "userName";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(articleEntityRepository.save(any())).thenReturn(mock(ArticleEntity.class));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> articleService.create(title,tag,description,disclosureType,userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트수정이_성공한경우() {
        Long articleId = 1L;
        String title = "title";
        String tag = "tag";
        String description = "설명입니다";
        DisclosureType disclosureType = VISIBLE;
        String userName = "userName";

        ArticleEntity articleEntity = ArticleEntityFixture.get(articleId, 1L, userName);
        UserEntity userEntity = articleEntity.getUser();

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.of(articleEntity));
        when(articleEntityRepository.saveAndFlush(any())).thenReturn(articleEntity);
        Assertions.assertDoesNotThrow(() -> articleService.modify(articleId, title, tag, description, disclosureType, userName));
    }

    @Test
    void 포스트수정시_포스트가_존재하지_않는_경우() {
        Long articleId = 1L;
        String title = "title";
        String tag = "tag";
        String description = "설명입니다";
        DisclosureType disclosureType = VISIBLE;
        String userName = "userName";

        ArticleEntity articleEntity = ArticleEntityFixture.get(articleId, 1L, userName);
        UserEntity userEntity = articleEntity.getUser();

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> articleService.modify(articleId, title, tag, description, disclosureType, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.ARTICLE_NOT_FOUND);
    }

    @Test
    void 포스트수정시_권한이_없는_경우() {
        Long articleId = 1L;
        String title = "title";
        String tag = "tag";
        String description = "설명입니다";
        DisclosureType disclosureType = VISIBLE;
        String userName = "userName";

        ArticleEntity articleEntity = ArticleEntityFixture.get(articleId, 1L, userName);
        UserEntity writer = UserEntityFixture.get(2L, "userName1", "password");
        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.of(articleEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> articleService.modify(articleId, title, tag, description, disclosureType, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.INVALID_PERMISSION);
    }

    @Test
    void 포스트삭제가_성공한경우() {
        Long articleId = 1L;
        String userName = "userName";

        ArticleEntity articleEntity = ArticleEntityFixture.get(articleId, 1L, userName);
        UserEntity userEntity = articleEntity.getUser();

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.of(articleEntity));

        Assertions.assertDoesNotThrow(() -> articleService.delete(articleId, userName));
    }

    @Test
    void 포스트삭제시_포스트가_존재하지_않는_경우() {
        Long articleId = 1L;
        String userName = "userName";

        ArticleEntity articleEntity = ArticleEntityFixture.get(articleId, 1L, userName);
        UserEntity userEntity = articleEntity.getUser();

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> articleService.delete(articleId, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.ARTICLE_NOT_FOUND);
    }

    @Test
    void 포스트삭제시_권한이_없는_경우() {
        Long articleId = 1L;
        String userName = "userName";

        ArticleEntity articleEntity = ArticleEntityFixture.get(articleId, 1L, userName);
        UserEntity writer = UserEntityFixture.get(2L, "userName1", "password");

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.of(articleEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> articleService.delete(articleId, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.INVALID_PERMISSION);
    }

    @Test
    void 피드목록_요청이_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        when(articleEntityRepository.findAll(pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> articleService.list(pageable));
    }

    @Test
    void 내_피드목록_요청이_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        UserEntity user = mock(UserEntity.class);
        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(articleEntityRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> articleService.my("", pageable));
    }

}