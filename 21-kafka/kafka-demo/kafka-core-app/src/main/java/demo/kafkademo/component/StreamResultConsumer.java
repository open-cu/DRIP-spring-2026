package demo.kafkademo.component;

import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.service.CoreUiStateService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StreamResultConsumer {
    private final CoreUiStateService stateService;

    public StreamResultConsumer(CoreUiStateService stateService) {
        this.stateService = stateService;
    }

    @KafkaListener(
            topics = KafkaTopicsConfig.STREAMS_RESULT,
            groupId = "kafka-core-stream-result-ui",
            containerFactory = "streamStringListenerContainerFactory"
    )
    void handle(String payload) {
        stateService.markStreamResult(payload);
    }
}
