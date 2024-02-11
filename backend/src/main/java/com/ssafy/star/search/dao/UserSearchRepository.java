package com.ssafy.star.search.dao;

import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSearchRepository extends JpaRepository<UserEntity, Long> {
    // 검색 - 닉네임
    Page<UserEntity> findByNicknameContaining(String keyword, Pageable pageable);
}
