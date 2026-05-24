package demo.kafkademo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
public class DelayedRetryConfig {
    public static final int MAX_ATTEMPTS = 3;
    public static final long RETRY_DELAY_MILLIS = 2_000L;

    /**
     * Конфигурация технического retry после ошибки обработки.
     * Важно: includeTopic ограничивает retry только process topic, maxAttempts задает число
     * попыток, fixedBackOff - паузу между ними, а DLT handler принимает окончательно
     * не обработанные сообщения.
     */
    @Bean
    RetryTopicConfiguration delayedProcessRetryTopicConfiguration(
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        // Это не механизм задержки. Здесь только повторы после ошибки в orders.delayed.process.
        return RetryTopicConfigurationBuilder
                .newInstance()
                .includeTopic(KafkaTopicsConfig.DELAYED_PROCESS)
                .maxAttempts(MAX_ATTEMPTS)
                .fixedBackOff(RETRY_DELAY_MILLIS)
                .useSingleTopicForSameIntervals()
                .retryTopicSuffix(".retry")
                .dltSuffix(".DLT")
                .autoCreateTopicsWith(3, (short) 1)
                .dltHandlerMethod("delayedProcessConsumer", "handleDlt")
                .create(kafkaTemplate);
    }
}
