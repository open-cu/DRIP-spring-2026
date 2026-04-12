package demo.rabbitmqdemo.config.slide27;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide27")
@Configuration(proxyBeanMethods = false)
public class Slide27OrdersTopologyConfig {

    @Bean
    public Declarables slide27OrdersTopology() {
        DirectExchange exchange = new DirectExchange("orders.direct");
        Queue queue = QueueBuilder.durable("orders.queue").build();
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("order.created");
        return new Declarables(exchange, queue, binding);
    }
}
