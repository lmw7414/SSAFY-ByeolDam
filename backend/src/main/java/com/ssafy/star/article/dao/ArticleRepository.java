package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
        Page<ArticleEntity> findAllByUser(UserEntity entity, Pageable pageable);

}

