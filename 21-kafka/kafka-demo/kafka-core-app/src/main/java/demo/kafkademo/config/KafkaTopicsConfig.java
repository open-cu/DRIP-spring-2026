package demo.kafkademo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {
    public static final String ORDERS_CREATED = "orders.created";
    public static final String STREAMS_INPUT = "orders.stream.input";
    public static final String STREAMS_RESULT = "orders.stream.result";

    @Bean
    NewTopic ordersCreatedTopic() {
        return TopicBuilder.name(ORDERS_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic streamsInputTopic() {
        return TopicBuilder.name(STREAMS_INPUT)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic streamsResultTopic() {
        return TopicBuilder.name(STREAMS_RESULT)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
