package demo.kafkademo.service;

import java.util.ArrayList;
import java.util.List;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.BatchOrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BatchScenarioService {
    private final BatchUiStateService stateService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BatchScenarioService(BatchUiStateService stateService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.stateService = stateService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBatch(int requestedCount) {
        int count = Math.min(20, Math.max(1, requestedCount));
        List<BatchOrderEvent> events = new ArrayList<>();
        for (int index = 1; index <= count; index++) {
            events.add(stateService.createOkEvent("batch заказ #" + index));
        }
        events.forEach(this::send);
    }

    public void publishFailureBatch() {
        List<BatchOrderEvent> events = List.of(
                stateService.createOkEvent("batch заказ до ошибки"),
                stateService.createFailingEvent("batch заказ fail"),
                stateService.createOkEvent("batch заказ после ошибки")
        );
        events.forEach(this::send);
    }

    public BatchUiStateService.PageData pageData() {
        return stateService.pageData();
    }

    public void reset() {
        stateService.reset();
    }

    private void send(BatchOrderEvent event) {
            kafkaTemplate.send(KafkaTopicsConfig.BATCH_CREATED, String.valueOf(event.orderId()), event);
    }
}
