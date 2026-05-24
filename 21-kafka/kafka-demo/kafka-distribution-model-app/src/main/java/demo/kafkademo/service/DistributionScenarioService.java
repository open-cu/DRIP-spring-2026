package demo.kafkademo.service;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.DistributionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DistributionScenarioService {
    private final DistributionUiStateService stateService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DistributionScenarioService(
            DistributionUiStateService stateService,
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.stateService = stateService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String key, String payload) {
        DistributionEvent event = stateService.createEvent(key, payload);
        kafkaTemplate.send(KafkaTopicsConfig.ORDERS_DISTRIBUTION, event.key(), event);
    }

    public DistributionUiStateService.PageData pageData() {
        return stateService.pageData();
    }

    public void reset() {
        stateService.reset();
    }
}
