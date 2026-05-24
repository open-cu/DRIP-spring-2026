package demo.rabbitmqdemo.config.blocks.routingbasics;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeadersExchangeConfig {

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange("orders.headers");
    }

    @Bean
    public Queue highPriorityQueue() {
        return QueueBuilder.durable("orders.headers.high.queue").build();
    }
    @Bean
    public Queue lowPriorityQueue() {
        return QueueBuilder.durable("orders.headers.low.queue").build();
    }

    @Bean
    public Binding highBinding(Queue highPriorityQueue, HeadersExchange headersExchange) {
        return BindingBuilder.bind(highPriorityQueue)
                .to(headersExchange)
                .whereAll(Map.of("X-Priority", "HIGH"))
                .match();
    }

    @Bean
    public Binding lowBinding(Queue lowPriorityQueue, HeadersExchange headersExchange) {
        return BindingBuilder.bind(lowPriorityQueue)
                .to(headersExchange)
                .whereAll(Map.of("X-Priority", "LOW"))
                .match();
    }

//    Пример создания топологии через Declarables
    public Declarables headersTopology() {
        HeadersExchange exchange = new HeadersExchange("orders.headers");
        Queue highPriorityQueue = QueueBuilder.durable("orders.headers.high.queue").build();
        Queue lowPriorityQueue = QueueBuilder.durable("orders.headers.low.queue").build();
        Binding highBinding = BindingBuilder.bind(highPriorityQueue)
                .to(exchange)
                .whereAll(Map.of("X-Priority", "HIGH"))
                .match();
        Binding lowBinding = BindingBuilder.bind(lowPriorityQueue)
                .to(exchange)
                .whereAll(Map.of("X-Priority", "LOW"))
                .match();
        return new Declarables(exchange, highPriorityQueue, highBinding, lowPriorityQueue, lowBinding);
    }
}
