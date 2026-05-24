package demo.kafkademo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {
    public static final String BATCH_CREATED = "orders.batch.created";
    public static final String BATCH_DLT = "orders.batch.created.DLT";

    @Bean
    NewTopic batchCreatedTopic() {
        return TopicBuilder.name(BATCH_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic batchDltTopic() {
        return TopicBuilder.name(BATCH_DLT)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
