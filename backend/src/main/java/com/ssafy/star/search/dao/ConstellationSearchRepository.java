package com.ssafy.star.search.dao;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstellationSearchRepository extends JpaRepository<ConstellationEntity, Long> {
    // 별자리 title
    List<ConstellationEntity> findByTitleContaining(String keyword, Sort sort);
}
