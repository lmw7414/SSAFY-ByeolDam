package com.ssafy.star.search.dao;

import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSearchRepository extends JpaRepository<UserEntity, Long> {
    // 검색 - 닉네임
    List<UserEntity> findByNicknameContaining(String keyword, Sort sort);
}
