package com.ssafy.star.notification.consumer;

import com.ssafy.star.notification.application.NotificationService;
import com.ssafy.star.notification.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.topic.notification}")
    public void consumeNotification(NotificationEvent event, Acknowledgment ack) {
        log.info("Consume the event {}", event);
        notificationService.send(event.getType(), event.getArgs(), event.getReceiverUserId());
        ack.acknowledge();
    }
}
