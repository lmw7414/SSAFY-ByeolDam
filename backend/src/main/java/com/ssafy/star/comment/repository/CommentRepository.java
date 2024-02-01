package com.ssafy.star.comment.repository;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByArticleEntity(ArticleEntity article, Pageable pageable);
    @Query("SELECT c FROM CommentEntity c WHERE c.articleEntity = :article AND c.parentId IS NULL")
    Page<CommentEntity> findByArticleEntityAndParentId(@Param("article")ArticleEntity article, Pageable pageable);
}
