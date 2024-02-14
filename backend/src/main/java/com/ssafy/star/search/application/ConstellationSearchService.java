package com.ssafy.star.search.application;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.contour.domain.ContourEntity;
import com.ssafy.star.contour.repository.ContourRepository;
import com.ssafy.star.search.dao.ConstellationSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ConstellationSearchService {
    private final ConstellationSearchRepository constellationSearchRepository;
    private final ContourRepository contourRepository;

    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    public ContourEntity findById(Long contourId) {
        return contourRepository.findById(contourId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.CONTOUR_NOT_FOUND));
    }

    public List<ConstellationEntity> constellationSearch(String keyword) {
        return constellationSearchRepository.findByNameContaining(keyword, sort);
    }
}
