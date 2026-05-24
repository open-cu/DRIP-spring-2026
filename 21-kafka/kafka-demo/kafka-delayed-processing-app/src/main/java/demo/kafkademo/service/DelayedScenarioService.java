package demo.kafkademo.service;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.DelayedOrderEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DelayedScenarioService {
    private final DelayedUiStateService stateService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DelayedScenarioService(DelayedUiStateService stateService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.stateService = stateService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createOrder(String description, int delaySeconds) {
        DelayedOrderEvent event = stateService.registerOrder(description, delaySeconds);
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                KafkaTopicsConfig.DELAYED_REQUEST,
                String.valueOf(event.orderId()),
                event
        );
        // Тема Messages With Delay: process-at говорит consumer-у, когда событие можно обрабатывать.
        record.headers().add(DelayedMessageHeaders.PROCESS_AT, DelayedMessageHeaders.processAtValue(event.processAt()));
        kafkaTemplate.send(record);
    }

    public DelayedUiStateService.PageData pageData() {
        return stateService.pageData();
    }

    public void reset() {
        stateService.reset();
    }
}
