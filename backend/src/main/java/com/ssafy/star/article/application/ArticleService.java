package com.ssafy.star.article.application;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.like.dao.ArticleLikeRepository;
import com.ssafy.star.like.domain.ArticleLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleLikeRepository articleLikeRepository;

    // 게시물 등록
    @Transactional
    public void create(String title, String tag, String description, DisclosureType disclosureType,
                       String email) {
        UserEntity userEntity = getUserEntityOrException(email);
        log.info("userEntity 정보 : {}", userEntity);
        articleRepository.save(ArticleEntity.of(title, tag, description, disclosureType, userEntity));
    }   // user 객체가 하는 역할이 뭔지?

    // 게시물 수정
    @Transactional
    public Article modify(Long articleId, String title, String tag, String description, DisclosureType disclosure,
                          String email) {

        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrException(email);

        if (articleEntity.getUser() != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", email, articleId));
        }

        articleEntity.setTitle(title);
        articleEntity.setTag(tag);
        articleEntity.setDescription(description);
        articleEntity.setDisclosure(disclosure);
        return Article.fromEntity(articleRepository.saveAndFlush(articleEntity));
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrException(email);
        // 삭제하려는 사람이 포스트 작성한 사람인지 확인
        if (articleEntity.getUser() != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", email, articleId));
        }
        articleRepository.delete(articleEntity);
    }

    // 게시물 전체 조회
    @Transactional(readOnly = true)
    public Page<Article> list(Pageable pageable) {
        return articleRepository.findAll(pageable).map(Article::fromEntity);
    }

    // 내 게시물 전체 조회
    @Transactional(readOnly = true)
    public Page<Article> my(String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrException(email);
        return articleRepository.findAllByUser(userEntity, pageable).map(Article::fromEntity);
    }

    // 게시물 상세 조회
    @Transactional(readOnly = true)
    public Article detail(Long articleId) {
        ArticleEntity articleEntity = articleRepository.findById(articleId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND, String.format("%s not founded", articleId)));
        articleEntity.setHits(articleEntity.getHits() + 1);

        return Article.fromEntity(articleRepository.save(articleEntity));
    }

    //게시물 좋아요 요청
    @Transactional
    public void like(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrException(email);

        // 좋아요 상태인지 확인
        articleLikeRepository.findByUserEntityAndArticleEntity(userEntity, articleEntity).ifPresentOrElse(
                articleLikeRepository::delete,
                () -> articleLikeRepository.save(ArticleLikeEntity.of(userEntity, articleEntity))
        );
    }

    //게시물 좋아요 상태 확인
    @Transactional
    public Boolean checkLike(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrException(email);

        //좋아요 상태인지 확인
        return articleLikeRepository.findByUserEntityAndArticleEntity(userEntity, articleEntity).isPresent();
    }

    //게시물 좋아요 갯수 확인
    @Transactional
    public Integer likeCount(Long articleId) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);

        //좋아요 갯수 확인
        return articleLikeRepository.countByArticleEntity(articleEntity);
    }

    //게시물 좋아요한 사람들의 목록 확인
    @Transactional
    public List<User> likeList(Long articleId) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        //목록 확인
        return articleLikeRepository.findAllByArticleEntity(articleEntity)
                .stream()
                .map(ArticleLikeEntity::getUserEntity)
                .map(User::fromEntity)
                .toList();
    }

    // 포스트가 존재하는지
    private ArticleEntity getArticleEntityOrException(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND, String.format("%s not founded", articleId)));
    }

    // 유저가 존재하는지
    private UserEntity getUserEntityOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));
    }

}
