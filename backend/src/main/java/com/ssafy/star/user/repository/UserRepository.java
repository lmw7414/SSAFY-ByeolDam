package com.ssafy.star.user.repository;

import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByNickname(String nickname);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
