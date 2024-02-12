package com.ssafy.star.constellation.dao;

import com.mongodb.lang.NonNull;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConstellationRepository extends JpaRepository<ConstellationEntity, Long> {

    @NonNull
    Optional<ConstellationEntity> findById(@NonNull Long id);

    // 나의 별자리 전체 조회
    @Query("SELECT cu.constellationEntity FROM ConstellationUserEntity cu WHERE cu.userEntity = :userEntity")
    List<ConstellationEntity> findAllByUserEntity(@Param("userEntity") UserEntity userEntity);


    //TODO : 별자리와 게시글(별)을 조인해서 보여줘야합니다.
}

