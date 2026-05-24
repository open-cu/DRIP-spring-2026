package demo.kafkademo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {
    public static final String ORDERS_DISTRIBUTION = "orders.distribution";

    @Bean
    NewTopic ordersDistributionTopic() {
        return TopicBuilder.name(ORDERS_DISTRIBUTION)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
