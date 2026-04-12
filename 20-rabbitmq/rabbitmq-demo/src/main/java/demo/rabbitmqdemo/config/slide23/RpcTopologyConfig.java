package demo.rabbitmqdemo.config.slide23;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide23")
@Configuration
public class RpcTopologyConfig {

    @Bean("directExchange")
    public DirectExchange directExchange() {
        return new DirectExchange("orders.direct");
    }

    @Bean
    public Queue orderQueryQueue() {
        return QueueBuilder.durable("order.query.queue").build();
    }

    @Bean
    public Queue orderResponsesQueue() {
        return QueueBuilder.durable("order.responses").build();
    }

    @Bean
    public Binding orderQueryBinding(@Qualifier("directExchange") DirectExchange ordersDirectExchange,
                                     Queue orderQueryQueue) {
        return BindingBuilder.bind(orderQueryQueue)
                .to(ordersDirectExchange)
                .with("order.query");
    }
}
