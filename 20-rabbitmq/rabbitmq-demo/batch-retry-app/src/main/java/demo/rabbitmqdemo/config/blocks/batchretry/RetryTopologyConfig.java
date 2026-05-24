package demo.rabbitmqdemo.config.blocks.batchretry;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RetryTopologyConfig {

    public static final String RETRY_DLX = "orders.retry.dlx";
    public static final String RETRY_QUEUE = "orders.retry.queue";
    public static final String RETRY_DLQ = "orders.retry.dlq";
    public static final String RETRY_DLQ_ROUTING_KEY = "orders.retry.dlq";

    @Bean
    public DirectExchange retryDlx() {
        return new DirectExchange(RETRY_DLX);
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(RETRY_QUEUE)
                .withArgument("x-dead-letter-exchange", RETRY_DLX)
                .withArgument("x-dead-letter-routing-key", RETRY_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue retryDlq() {
        return QueueBuilder.durable(RETRY_DLQ).build();
    }

    @Bean
    public Binding retryDlqBinding() {
        return BindingBuilder.bind(retryDlq()).to(retryDlx()).with(RETRY_DLQ_ROUTING_KEY);
    }
}
