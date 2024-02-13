package com.ssafy.star.contour.application;

import com.ssafy.star.contour.domain.ContourEntity;
import com.ssafy.star.contour.repository.ContourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContourService {
    private final ContourRepository contourRepository;


    // 서버에서 넘어오는 데이터 받아서 mongoDB에 저장 그리고 ID 리턴
    public Long insertContour(ContourEntity entity) {
        contourRepository.save(entity);
        return null;
    }


    //
}
