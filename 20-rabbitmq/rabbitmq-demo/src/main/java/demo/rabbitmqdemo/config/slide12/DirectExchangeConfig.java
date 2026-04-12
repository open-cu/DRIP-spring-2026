package demo.rabbitmqdemo.config.slide12;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide12")
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
    public Declarables directTopology(DirectExchange directExchange, Queue orderQueue) {
        Binding orderBinding = BindingBuilder.bind(orderQueue)
                .to(directExchange)
                .with("order.created");

        Binding billingBinding = BindingBuilder.bind(billingQueue())
                .to(directExchange)
                .with("order.paid");
        return new Declarables(directExchange, orderQueue, orderBinding, billingBinding);
    }
}
