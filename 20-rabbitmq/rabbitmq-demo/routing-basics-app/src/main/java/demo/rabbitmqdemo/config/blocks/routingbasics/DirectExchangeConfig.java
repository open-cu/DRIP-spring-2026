package demo.rabbitmqdemo.config.blocks.routingbasics;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("orders.direct");
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable("orders.queue").build();
    }

    @Bean
    public Queue billingQueue() {
        return QueueBuilder.durable("billing.queue").build();
    }


    @Bean
    public Binding orderBinding(DirectExchange directExchange, Queue orderQueue) {
        return BindingBuilder.bind(orderQueue)
                .to(directExchange)
                .with("order.created");
    }

    @Bean
    public Binding billingBinding(DirectExchange directExchange, Queue billingQueue) {
        return BindingBuilder.bind(billingQueue)
                .to(directExchange)
                .with("order.paid");
    }
}
