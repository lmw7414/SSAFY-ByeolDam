package com.ssafy.star.notification.producer;

import com.ssafy.star.notification.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<Long, NotificationEvent> notificationEventKafkaTemplate;

    @Value("${spring.kafka.topic.notification}")
    private String topic;

    public void send(NotificationEvent notificationEvent) {
        notificationEventKafkaTemplate.send(topic, notificationEvent.getReceiverUserId(), notificationEvent);
        log.info("send fin");
    }
}