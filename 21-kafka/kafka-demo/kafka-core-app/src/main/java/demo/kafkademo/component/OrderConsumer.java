package demo.kafkademo.component;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.OrderCreatedEvent;
import demo.kafkademo.service.CoreUiStateService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    private final CoreUiStateService stateService;

    public OrderConsumer(CoreUiStateService stateService) {
        this.stateService = stateService;
    }

    @KafkaListener(topics = KafkaTopicsConfig.ORDERS_CREATED, groupId = "kafka-core-order-ui")
    void handle(OrderCreatedEvent event) {
        stateService.markOrderProcessed(event);
    }
}
