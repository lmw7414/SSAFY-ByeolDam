package com.ssafy.star.constellation.dao;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConstellationLikeRepository extends JpaRepository<ConstellationLikeEntity, Long> {
    Optional<ConstellationLikeEntity> findByUserEntityAndConstellationEntity(UserEntity userEntity, ConstellationEntity constellationEntity);

    @Query(value = "SELECT COUNT(*) FROM ConstellationLikeEntity entity WHERE entity.constellationEntity =:constellationEntity")
    Integer countByConstellationEntity(ConstellationEntity constellationEntity);
    void deleteAllByConstellationEntity(ConstellationEntity constellationEntity);
    void deleteAllByUserEntity(UserEntity userEntity);

    List<ConstellationLikeEntity> findAllByConstellationEntity(ConstellationEntity constellationEntity);
    Page<ConstellationLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable);
}
