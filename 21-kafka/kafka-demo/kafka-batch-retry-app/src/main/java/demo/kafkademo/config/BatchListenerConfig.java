package demo.kafkademo.config;

import demo.kafkademo.core.model.BatchOrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class BatchListenerConfig {
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, BatchOrderEvent> batchKafkaListenerContainerFactory(
            ConsumerFactory<String, BatchOrderEvent> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, BatchOrderEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        return factory;
    }
}
