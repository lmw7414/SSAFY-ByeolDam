package com.ssafy.star.contour.utils;

import com.ssafy.star.contour.domain.ContourEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContourListener extends AbstractMongoEventListener<ContourEntity> {
    private final SequenceGeneratorService generatorService;

    // 새로운 엔티티가 추가될 때마다 id값이 자동으로 증가하도록 설정
    // 참고 : https://zzang9ha.tistory.com/384
    @Override
    public void onBeforeConvert(BeforeConvertEvent<ContourEntity> event) {
        event.getSource().set_id(generatorService.generateSequence(ContourEntity.SEQUENCE_NAME));
    }
}
