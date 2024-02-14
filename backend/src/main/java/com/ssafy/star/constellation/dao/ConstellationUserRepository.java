package com.ssafy.star.constellation.dao;

import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConstellationUserRepository extends JpaRepository<ConstellationUserEntity, Long> {

    // 별자리 삭제, 강퇴, 유저 역할 변경
    @Query("SELECT a FROM ConstellationUserEntity a WHERE a.userEntity = :userEntity AND a.constellationEntity = :constellationEntity")
    Optional<ConstellationUserEntity> findByUserEntityAndConstellationEntity(@Param("userEntity") UserEntity userEntity, @Param("constellationEntity") ConstellationEntity constellationEntity);

    // 해당 유저가 속한 모든 별자리 보기(USER든 ADMIN이든 상관 없이)
    @Query("SELECT cu FROM ConstellationUserEntity cu WHERE cu.userEntity = :userEntity")
    List<ConstellationUserEntity> findConstellationUserEntitiesByUserEntity(@Param("userEntity") UserEntity userEntity);

    // 해당 별자리에 속한 모든 유저 보기
    @Query("SELECT cu FROM ConstellationUserEntity cu WHERE cu.constellationEntity = :constellationEntity")
    List<ConstellationUserEntity> findConstellationUserEntitiesByConstellationEntity(@Param("constellationEntity") ConstellationEntity constellationEntity);

    // 해당 유저의 별자리 가져오기
    @Query("SELECT c FROM ConstellationUserEntity cu JOIN ConstellationEntity c  ON c.id = cu.constellationEntity.id  WHERE cu.userEntity =:userEntity")
    List<ConstellationEntity> findConstellationByUserEntity(@Param("userEntity") UserEntity userEntity);

    @Query(value = "SELECT COUNT(*) FROM ConstellationUserEntity entity WHERE entity.userEntity = :userEntity")
    Integer countConstellationByUser(@Param("userEntity") UserEntity userEntity);

    List<ConstellationUserEntity> findByUserEntityAndConstellationUserRole(UserEntity userEntity, ConstellationUserRole constellationUserRole);
}
