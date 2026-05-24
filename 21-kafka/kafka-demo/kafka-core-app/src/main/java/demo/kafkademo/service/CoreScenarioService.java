package demo.kafkademo.service;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CoreScenarioService {
    private final CoreUiStateService stateService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    public CoreScenarioService(
            CoreUiStateService stateService,
            KafkaTemplate<String, Object> kafkaTemplate,
            @Qualifier("stringKafkaTemplate") KafkaTemplate<String, String> stringKafkaTemplate
    ) {
        this.stateService = stateService;
        this.kafkaTemplate = kafkaTemplate;
        this.stringKafkaTemplate = stringKafkaTemplate;
    }

    public void createOrder(String description) {
        OrderCreatedEvent event = stateService.registerOrder(description);
        kafkaTemplate.send(KafkaTopicsConfig.ORDERS_CREATED, String.valueOf(event.orderId()), event);
    }

    public void sendToStreams(String description) {
        CoreUiStateService.StreamInput input = stateService.registerStreamInput(description);
        stringKafkaTemplate.send(KafkaTopicsConfig.STREAMS_INPUT, String.valueOf(input.id()), input.payload());
    }

    public CoreUiStateService.PageData pageData() {
        return stateService.pageData();
    }

    public void reset() {
        stateService.reset();
    }
}
