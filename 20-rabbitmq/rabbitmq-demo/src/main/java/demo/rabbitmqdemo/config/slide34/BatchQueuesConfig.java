package demo.rabbitmqdemo.config.slide34;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("slide34")
@Configuration
@EnableScheduling
public class BatchQueuesConfig {

    @Bean
    public Queue batchQueue() {
        return QueueBuilder.durable("batch.queue").build();
    }
}
