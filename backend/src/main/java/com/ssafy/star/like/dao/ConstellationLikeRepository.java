package com.ssafy.star.like.dao;

import com.ssafy.star.like.domain.ConstellationLikeEntity;
import com.ssafy.star.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConstellationLikeRepository extends JpaRepository<ConstellationLikeEntity, Long> {
    Optional<ConstellationLikeEntity> findByUserAndConstellation(UserEntity userEntity, ConstellationEntity constellationEntity);

    @Query(value = "SELECT COUNT(*) FROM ConstellationLikeEntity entity WHERE entity.constellationEntity =:constellationEntity")
    Integer countByConstellation(@Param("constellation") ConstellationEntity constellationEntity);

    List<ConstellationLikeEntity> findAllByConstellation(ConstellationEntity constellationEntity);
}
