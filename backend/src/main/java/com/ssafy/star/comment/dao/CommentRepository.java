package com.ssafy.star.comment.dao;

import com.ssafy.star.article.ArticleEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByArticleEntity(ArticleEntity article, Pageable pageable);
}
