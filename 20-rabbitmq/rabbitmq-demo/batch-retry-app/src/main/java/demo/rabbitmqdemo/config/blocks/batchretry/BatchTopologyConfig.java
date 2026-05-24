package demo.rabbitmqdemo.config.blocks.batchretry;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class BatchTopologyConfig {

    public static final String BATCH_QUEUE = "batch.queue";

    @Bean
    public Queue batchQueue() {
        return QueueBuilder.durable(BATCH_QUEUE).build();
    }
}
