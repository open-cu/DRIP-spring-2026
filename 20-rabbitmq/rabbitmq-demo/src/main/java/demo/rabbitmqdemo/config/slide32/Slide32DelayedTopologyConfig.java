package demo.rabbitmqdemo.config.slide32;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide32")
@Configuration(proxyBeanMethods = false)
public class Slide32DelayedTopologyConfig {

    @Bean
    public DirectExchange delayedExchange() {
        return new DirectExchange("delayed.exchange");
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable("delayed.queue")
                .withArgument("x-message-ttl", 30_000)
                .withArgument("x-dead-letter-exchange", "delayed.exchange")
                .withArgument("x-dead-letter-routing-key", "process.order")
                .build();
    }

    @Bean
    public Queue delayed5sQueue() {
        return QueueBuilder.durable("delayed.5s.queue")
                .withArgument("x-message-ttl", 5_000)
                .withArgument("x-dead-letter-exchange", "delayed.exchange")
                .withArgument("x-dead-letter-routing-key", "process.order")
                .build();
    }

    @Bean
    public Queue delayed30sQueue() {
        return QueueBuilder.durable("delayed.30s.queue")
                .withArgument("x-message-ttl", 30_000)
                .withArgument("x-dead-letter-exchange", "delayed.exchange")
                .withArgument("x-dead-letter-routing-key", "process.order")
                .build();
    }

    @Bean
    public Queue delayed5mQueue() {
        return QueueBuilder.durable("delayed.5m.queue")
                .withArgument("x-message-ttl", 300_000)
                .withArgument("x-dead-letter-exchange", "delayed.exchange")
                .withArgument("x-dead-letter-routing-key", "process.order")
                .build();
    }

    @Bean
    public Queue processQueue() {
        return QueueBuilder.durable("process.queue").build();
    }

    @Bean
    public Binding delayedBinding(Queue processQueue, DirectExchange delayedExchange) {
        return BindingBuilder.bind(processQueue)
                .to(delayedExchange)
                .with("process.order");
    }
}
