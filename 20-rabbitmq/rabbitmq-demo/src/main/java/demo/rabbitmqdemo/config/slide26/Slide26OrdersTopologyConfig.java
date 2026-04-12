package demo.rabbitmqdemo.config.slide26;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide26")
@Configuration(proxyBeanMethods = false)
public class Slide26OrdersTopologyConfig {

    @Bean
    public Declarables slide26OrdersTopology() {
        DirectExchange exchange = new DirectExchange("orders.direct");
        Queue queue = QueueBuilder.durable("orders.queue").build();
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("order.created");
        return new Declarables(exchange, queue, binding);
    }
}
