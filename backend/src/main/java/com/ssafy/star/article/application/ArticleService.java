package com.ssafy.star.article.application;

import com.ssafy.star.article.dao.ArticleHashtagRelationRepository;
import com.ssafy.star.article.dao.ArticleLikeRepository;
import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import com.ssafy.star.article.domain.ArticleLikeEntity;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.common.infra.S3.S3uploader;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.application.ImageService;
import com.ssafy.star.image.domain.ImageEntity;
import com.ssafy.star.notification.dto.NotificationArgs;
import com.ssafy.star.notification.dto.NotificationEvent;
import com.ssafy.star.notification.dto.NotificationType;
import com.ssafy.star.notification.producer.NotificationProducer;
import com.ssafy.star.user.domain.FollowEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleHashtagRelationRepository articleHashtagRelationRepository;
    private final ConstellationRepository constellationRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final S3uploader s3uploader;
    private final ImageService imageService;
    private final ArticleHashtagRelationService articleHashtagRelationService;
    private final ArticleLikeRepository articleLikeRepository;
    private final NotificationProducer notificationProducer;

    /**
     * 게시물 등록과 별자리 배정
      */
    @Transactional
    public void create(
            String title,
            String description,
            DisclosureType disclosureType,
            String email,
            MultipartFile imageFile,
            ImageType imageType,
            Set<String> articleHashtagSet,
            Long constellationId
    ) {
        String url = "";
        String thumbnailUrl = "";
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId); // 배정하려는 별자리 Entity

        // 별자리 회원인지 확인하기
        if(constellationEntity.getAdminEntity() != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with constellation %d", userEntity.getNickname(), constellationId));
        }

        // 이미지 S3에 업로드 (가장 마지막에 놓을 것)
        try{
            url = s3uploader.upload(imageFile, "articles");
            thumbnailUrl = s3uploader.uploadThumbnail(imageFile, "thumbnails");
            ImageEntity imageEntity = imageService.saveImage(imageFile.getOriginalFilename(), url, thumbnailUrl, imageType);

            ArticleEntity articleEntity = ArticleEntity.of(title,
                    description,
                    disclosureType,
                    userEntity,
                    null,
                    imageEntity
            );

            articleRepository.save(articleEntity);

            articleHashtagRelationService.saveHashtag(articleEntity, articleHashtagSet);

            articleEntity.selectConstellation(constellationEntity);
        } catch (IOException e) {
            s3uploader.deleteImageFromS3(url);
            s3uploader.deleteImageFromS3(thumbnailUrl);
        }
    }

    /**
     * 게시물 등록 및 미분류 별자리 배정
     */
    @Transactional
    public void createWithNoConstellation(
            String title,
            String description,
            DisclosureType disclosureType,
            String email,
            MultipartFile imageFile,
            ImageType imageType,
            Set<String> articleHashtagSet
    ) {
        String url = "";
        String thumbnailUrl = "";
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);

        try {

            url = s3uploader.upload(imageFile, "articles");
            thumbnailUrl = s3uploader.uploadThumbnail(imageFile, "thumbnails");
            ImageEntity imageEntity = imageService.saveImage(imageFile.getOriginalFilename(), url, thumbnailUrl, imageType);

            ArticleEntity articleEntity = ArticleEntity.of(title,
                    description,
                    disclosureType,
                    userEntity,
                    null,
                    imageEntity
            );

            articleRepository.save(articleEntity);

            articleHashtagRelationService.saveHashtag(articleEntity, articleHashtagSet);
        } catch (IOException e) {
            s3uploader.deleteImageFromS3(url);
            s3uploader.deleteImageFromS3(thumbnailUrl);
        }
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public void modify(
            Long articleId,
            String title,
            String description,
            DisclosureType disclosure,
            String email,
            Set<String> articleHashtagSet
    ) {
        // 게시물 owner가 맞는지 확인
        ArticleEntity articleEntity = getArticleOwnerOrException(articleId, email);

        articleHashtagRelationService.deleteByArticleEntity(articleEntity);
        articleHashtagRelationService.saveHashtag(articleEntity, articleHashtagSet);

        articleEntity.update(title, description, disclosure);
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public void delete(Long articleId, String email) {
        // 게시물 owner가 맞는지 확인
        ArticleEntity articleEntity = getArticleOwnerOrException(articleId, email);
        if(articleEntity.getDeletedAt() != null) {
            throw new ByeolDamException(ErrorCode.ARTICLE_DELETED, String.format("article %d has already deleted", articleId));
        } else {
            articleLikeRepository.deleteAllByArticleEntity(articleEntity);
            articleRepository.delete(articleEntity);
            articleHashtagRelationService.deleteByArticleEntity(articleEntity);
        }
    }

    /**
     * 휴지통 조회
     */
    @Transactional
    public Page<Article> trashcan(String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        return articleRepository.findAllByOwnerEntityAndDeleted(userEntity, pageable).map(Article::fromEntity);
    }

    /**
     * 휴지통에 있는 게시물 복원
     */
    @Transactional
    public Article undoDeletion(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);

        if(!articleEntity.getOwnerEntity().equals(userEntity)) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission", userEntity.getNickname()));
        }

        if(articleEntity.getDeletedAt() != null) {
            articleEntity.undoDeletion();
            articleLikeRepository.findAllByArticleEntity(articleEntity).forEach(ArticleLikeEntity::undoDeletion);
            articleHashtagRelationRepository.findAllByArticleEntity(articleEntity).forEach(ArticleHashtagRelationEntity::undoDeletion);

        } else {
            throw new ByeolDamException(ErrorCode.INVALID_REQUEST, String.format("article %d is not abandoned", articleId));
        }

        return Article.fromEntity(articleEntity);
    }

    /**
     * 팔로우 피드
     */
    // 팔로우한 사람들의 게시물들을 최신순으로 나열해서 보여준다
    @Transactional(readOnly = true)
    public Page<Article> followFeed(String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        Set<UserEntity> userEntitySet = userEntity.getFollowEntities().stream().map(FollowEntity::getToUser).collect(Collectors.toSet());

        List<ArticleEntity> articleEntityList = new ArrayList<>();
        // ID별 모든 게시글 가져오기
        for (UserEntity tmpUserEntity : userEntitySet) {
            List<ArticleEntity> tmpArticleEntities = tmpUserEntity.getArticleEntities();
            articleEntityList.addAll(tmpArticleEntities);
        }

        // 최신순 정렬
        Collections.sort(articleEntityList, new Comparator<ArticleEntity>() {
            @Override
            public int compare(ArticleEntity a1, ArticleEntity a2) {
                return a2.getCreatedAt().compareTo(a1.getCreatedAt());
            }
        });

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), articleEntityList.size());
        List<Article> dtoList = articleEntityList.subList(start, end)
                .stream()
                .map(Article::fromEntity)
                .collect(Collectors.toList());

        // 페이징처리
        return new PageImpl<>(dtoList, pageable, articleEntityList.size());
    }

    /**
     * 유저의 게시물 전체 조회
     */
    @Transactional(readOnly = true)
    public List<Article> userArticleList(String nickname, String email) {
        // 찾는 유저가 접속자라면 전체 조회한다
        UserEntity myEntity = getUserEntityOrExceptionByEmail(email);
        UserEntity userEntity = getUserEntityOrExceptionByNickname(nickname);
        if (!myEntity.equals(userEntity)) {
            if (!followRepository.findByFromUserAndToUser(myEntity, userEntity).isPresent()) {
                // disclosureType에 따라 조회여부 판단
                return articleRepository.findAllByOwnerEntityAndNotDeletedAndDisclosure(userEntity).stream().map(Article::fromEntity).toList();
            }
            // following 중이라면 전체 조회한다
        }
        return articleRepository.findAllByOwnerEntityAndNotDeleted(userEntity).stream().map(Article::fromEntity).toList();
    }

    /**
     * 게시물 상세 조회
     */
    @Transactional(readOnly = true)
    public Article detail(Long articleId, String email) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);

        // 해당 article이 없을 경우 예외처리
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);

        // articleId AND deletedAt == null AND (내 게시물이거나 VISIBLE)
        if (articleRepository.findByArticleIdAndNotDeleted(articleId, userEntity)) {
            articleEntity.addHits();

            return Article.fromEntity(articleRepository.saveAndFlush(articleEntity));
        } else {
            // Deletion 예외처리
            if(articleEntity.getDeletedAt() != null) {
                throw new ByeolDamException(ErrorCode.ARTICLE_DELETED, String.format("article %d has deleted", articleId));

            // 볼 수 있는 권한이 없다(내 게시물이 아니거나 INVISIBLE)
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with article %d", userEntity.getNickname(), articleId));
        }
    }

    /**
     * 별자리에 게시물 배정
     */
    @Transactional
    public void select(Long constellationId, Set<Long> articleIdSet, String email) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);                      // 현재 사용자 user entity
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId); // 배정하려는 별자리 Entity

        // 별자리 회원인지 확인하기
        if (constellationEntity.getAdminEntity() != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with constellation %d", userEntity.getNickname(), constellationId));
        }

        // 반복문을 통해 Set에 있는 article 전부 별자리에 배정
        for (Long articleId : articleIdSet) {
            ArticleEntity articleEntity = getArticleEntityOrException(articleId);
            UserEntity ownerEntity = articleEntity.getOwnerEntity();                  // admin의 user entity

            // article이 휴지통에 있다면 예외처리
            if (articleEntity.getDeletedAt() != null) {
                throw new ByeolDamException(ErrorCode.ARTICLE_DELETED,
                        String.format("article %d deleted", articleId));
            }

            // article 본인 것이 아니라면 예외처리
            if (ownerEntity != userEntity) {
                throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                        String.format("%s has no permission with %d", userEntity.getNickname(), articleId));
            }

            articleEntity.selectConstellation(constellationEntity);
        }
    }

    /**
     * 별자리의 전체 게시물 조회
     */
    @Transactional
    public List<Article> articlesInConstellation(Long constellationId, String email) {
        // email로 userEntity 구하고 별자리 공개여부와 해당 게시물 공유여부를 확인해 Error 반환
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);
        return articleRepository.findAllByConstellationEntity(constellationEntity, userEntity).stream().map(Article::fromEntity).toList();
    }

    /**
     * 미분류 별자리의 전체 게시물 조회
     */
    @Transactional
    public List<Article> articlesInNoConstellation(String email) {
        // email로 userEntity 구하고 별자리 공개여부와 해당 게시물 공유여부를 확인해 Error 반환
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        return articleRepository.findAllByConstellationEntityNullAndOwnerEntity(userEntity).stream().map(Article::fromEntity).toList();
    }

    // 포스트가 존재하는지
    private ArticleEntity getArticleEntityOrException(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND, String.format("article %d not founded", articleId)));
    }

    // 유저가 존재하는지(email)
    private UserEntity getUserEntityOrExceptionByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("email %s not founded", email)));
    }

    // 유저가 존재하는지(nickname)
    private UserEntity getUserEntityOrExceptionByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", nickname)));
    }

    // 별자리가 존재하는지
    private ConstellationEntity getConstellationEntityOrException(Long constellationId) {
        return constellationRepository.findById(constellationId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.CONSTELLATION_NOT_FOUND, String.format("constellation %d not founded", constellationId)));
    }

    // 게시물 owner인지 확인
    private ArticleEntity getArticleOwnerOrException(Long articleId, String email) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);                                    // 현재 사용자 user entity
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity ownerEntity = articleEntity.getOwnerEntity();                              // admin의 user entity

        // admin이어야 삭제 가능
        if (ownerEntity != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %d", userEntity.getNickname(), articleId));
        }

        return articleEntity;
    }


    //게시물 좋아요 요청
    @Transactional
    public void like(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);

        // 좋아요 상태인지 확인
        articleLikeRepository.findByUserEntityAndArticleEntity(userEntity, articleEntity).ifPresentOrElse(
                articleLikeRepository::delete,
                () -> {
                    articleLikeRepository.save(ArticleLikeEntity.of(userEntity, articleEntity));
                    // 자신의 게시물이 아닌경우, 좋아요 알람보내기
                    if (!articleEntity.getOwnerEntity().equals(userEntity)) {
                        notificationProducer.send(new NotificationEvent(NotificationType.NEW_LIKE_ON_POST, new NotificationArgs(userEntity.getId(), articleId), articleEntity.getOwnerEntity().getId()));
                    }
                });
    }

    //게시물 좋아요 상태 확인
    @Transactional
    public Boolean checkLike(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);

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

    public int countArticles(String email) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        return articleRepository.countArticlesByUser(userEntity);
    }
}
