package demo.kafkademo.component;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.DelayedOrderEvent;
import demo.kafkademo.service.DelayedUiStateService;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class DelayedProcessConsumer {
    private final DelayedUiStateService stateService;

    public DelayedProcessConsumer(DelayedUiStateService stateService) {
        this.stateService = stateService;
    }

    @KafkaListener(topics = KafkaTopicsConfig.DELAYED_PROCESS, groupId = "kafka-delayed-process-ui")
    void handle(
            DelayedOrderEvent event,
            Acknowledgment acknowledgment,
            @Header(name = KafkaHeaders.DELIVERY_ATTEMPT, required = false) Integer deliveryAttempt
    ) {
        String lowerCase = event.description().toLowerCase();
        if (lowerCase.contains("fail")) {
            // Ветка retry: это ошибка после задержки, а не механизм отложенной обработки.
            stateService.markRetryAttempt(event, deliveryAttempt == null ? 1 : deliveryAttempt);
            throw new IllegalStateException("Demo processing failure for order " + event.orderId());
        }
        // Обычный путь: задержка уже закончилась в DelayedRequestConsumer.
        stateService.markProcessed(event);
        acknowledgment.acknowledge();
    }

    public void handleDlt(DelayedOrderEvent event, Acknowledgment acknowledgment) {
        // DLT относится к retry после ошибки, а не к самой теме Messages With Delay.
        stateService.markSentToDlt(event);
        acknowledgment.acknowledge();
    }
}
