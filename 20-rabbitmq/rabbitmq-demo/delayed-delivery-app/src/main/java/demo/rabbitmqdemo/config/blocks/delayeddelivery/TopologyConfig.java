package demo.rabbitmqdemo.config.blocks.delayeddelivery;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TopologyConfig {

    public static final String EXCHANGE_WAIT = "wait.exchange";
    public static final String EXCHANGE_DELAYED = "delayed.exchange";
    public static final String QUEUE_DELAYED_DEFAULT = "delayed.queue";
    public static final String QUEUE_DELAYED_5S = "delayed.5s.queue";
    public static final String QUEUE_DELAYED_30S = "delayed.30s.queue";
    public static final String QUEUE_DELAYED_5M = "delayed.5m.queue";
    public static final String QUEUE_PROCESS = "process.queue";
    public static final String ROUTING_KEY_DELAY_5S = "delay.5s";
    public static final String ROUTING_KEY_DELAY_30S = "delay.30s";
    public static final String ROUTING_KEY_DELAY_5M = "delay.5m";
    public static final String ROUTING_KEY_PROCESS = "process.order";

    @Bean
    public DirectExchange waitExchange() {
        return new DirectExchange(EXCHANGE_WAIT);
    }

    @Bean
    public DirectExchange delayedExchange() {
        return new DirectExchange(EXCHANGE_DELAYED);
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(QUEUE_DELAYED_DEFAULT)
                .withArgument("x-message-ttl", 30_000)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DELAYED)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_PROCESS)
                .build();
    }

    @Bean
    public Queue delayed5sQueue() {
        return QueueBuilder.durable(QUEUE_DELAYED_5S)
                .withArgument("x-message-ttl", 5_000)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DELAYED)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_PROCESS)
                .build();
    }

    @Bean
    public Queue delayed30sQueue() {
        return QueueBuilder.durable(QUEUE_DELAYED_30S)
                .withArgument("x-message-ttl", 30_000)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DELAYED)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_PROCESS)
                .build();
    }

    @Bean
    public Queue delayed5mQueue() {
        return QueueBuilder.durable(QUEUE_DELAYED_5M)
                .withArgument("x-message-ttl", 300_000)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DELAYED)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_PROCESS)
                .build();
    }

    @Bean
    public Queue processQueue() {
        return QueueBuilder.durable(QUEUE_PROCESS).build();
    }

    @Bean
    public Binding delayed5sBinding(Queue delayed5sQueue, DirectExchange waitExchange) {
        return BindingBuilder.bind(delayed5sQueue).to(waitExchange).with(ROUTING_KEY_DELAY_5S);
    }

    @Bean
    public Binding delayed30sBinding(Queue delayed30sQueue, DirectExchange waitExchange) {
        return BindingBuilder.bind(delayed30sQueue).to(waitExchange).with(ROUTING_KEY_DELAY_30S);
    }

    @Bean
    public Binding delayed5mBinding(Queue delayed5mQueue, DirectExchange waitExchange) {
        return BindingBuilder.bind(delayed5mQueue).to(waitExchange).with(ROUTING_KEY_DELAY_5M);
    }

    @Bean
    public Binding delayedBinding(Queue processQueue, DirectExchange delayedExchange) {
        return BindingBuilder.bind(processQueue).to(delayedExchange).with(ROUTING_KEY_PROCESS);
    }
}
