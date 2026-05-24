package demo.kafkademo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {
    // Тема Messages With Delay начинается здесь: записи ждут в topic до process-at.
    public static final String DELAYED_REQUEST = "orders.delayed.request";
    // Обычная обработка начинается после истечения задержки.
    public static final String DELAYED_PROCESS = "orders.delayed.process";
    // Retry/DLT topic: отдельная тема, не механизм задержки.
    public static final String DELAYED_DLT = "orders.delayed.process.DLT";

    /**
     * Topic для входящих заказов, которые еще рано обрабатывать.
     * Важно: partitions=3 показывает, что pause/resume применяется к partition,
     * replicas=1 оставлено для локального demo-стенда без Kafka-кластера.
     */
    @Bean
    NewTopic delayedRequestTopic() {
        return TopicBuilder.name(DELAYED_REQUEST)
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * Topic для обычной обработки после наступления processAt.
     * Важно: сюда сообщение попадает только после задержки; технический retry
     * начинается уже отсюда, а не из request topic.
     */
    @Bean
    NewTopic delayedProcessTopic() {
        return TopicBuilder.name(DELAYED_PROCESS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * DLT topic для сообщений, которые не обработались после retry.
     * Важно: это финальная диагностическая зона для demo; причина попадания сюда -
     * ошибка обработки, а не сама бизнес-задержка.
     */
    @Bean
    NewTopic delayedDltTopic() {
        return TopicBuilder.name(DELAYED_DLT)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
