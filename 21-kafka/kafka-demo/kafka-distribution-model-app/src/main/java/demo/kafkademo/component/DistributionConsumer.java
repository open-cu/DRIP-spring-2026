package demo.kafkademo.component;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.DistributionEvent;
import demo.kafkademo.service.DistributionUiStateService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DistributionConsumer {
    private final DistributionUiStateService stateService;

    public DistributionConsumer(DistributionUiStateService stateService) {
        this.stateService = stateService;
    }

    @KafkaListener(topics = KafkaTopicsConfig.ORDERS_DISTRIBUTION, groupId = "kafka-distribution-ui", concurrency = "2")
    void handle(ConsumerRecord<String, DistributionEvent> record) {
        stateService.markReceived(
                record.value(),
                record.partition(),
                record.offset(),
                Thread.currentThread().getName()
        );
    }
}
