package demo.rabbitmqdemo.config.slide15;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide15")
@Configuration
public class HeadersExchangeConfig {

    @Bean
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

