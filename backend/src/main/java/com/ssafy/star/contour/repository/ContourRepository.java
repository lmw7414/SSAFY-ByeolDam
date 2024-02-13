package com.ssafy.star.contour.repository;

import com.mongodb.lang.NonNull;
import com.ssafy.star.contour.domain.ContourEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContourRepository extends MongoRepository<ContourEntity, String> {
    @NonNull
    Optional<ContourEntity> findById(@NonNull String contourId);
}
