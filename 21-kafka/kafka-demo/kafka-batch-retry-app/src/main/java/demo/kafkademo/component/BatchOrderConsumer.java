package demo.kafkademo.component;

import java.util.List;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.BatchOrderEvent;
import demo.kafkademo.service.BatchUiStateService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BatchOrderConsumer {
    private final BatchUiStateService stateService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BatchOrderConsumer(BatchUiStateService stateService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.stateService = stateService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = KafkaTopicsConfig.BATCH_CREATED,
            groupId = "kafka-batch-ui",
            containerFactory = "batchKafkaListenerContainerFactory"
    )
    void handle(List<BatchOrderEvent> events) {
        stateService.markBatchReceived(events.size());
        boolean batchHasFailure = events.stream().anyMatch(BatchOrderEvent::failForDemo);
        for (BatchOrderEvent event : events) {
            if (event.failForDemo()) {
                stateService.markFailed(event, "одно событие ломает пачку");
                kafkaTemplate.send(KafkaTopicsConfig.BATCH_DLT, String.valueOf(event.orderId()), event);
            } else if (batchHasFailure) {
                stateService.markProcessed(event, "обработан, но пачка уже считается рискованной");
            } else {
                stateService.markProcessed(event, "обычная batch-обработка");
            }
        }
    }

    @KafkaListener(topics = KafkaTopicsConfig.BATCH_DLT, groupId = "kafka-batch-dlt-ui")
    void handleDlt(BatchOrderEvent event) {
        stateService.markDlt(event);
    }
}
