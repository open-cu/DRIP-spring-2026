package demo.rabbitmqdemo.config.slide13;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide13")
@Configuration
public class TopicExchangeConfig {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("common.topic");
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable("orders.queue").build();
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable("notifications.queue").build();
    }

    @Bean
    public Binding topicOrderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(topicExchange())
                .with("order.*");  // * - одно слово, # - ноль или более
    }

    @Bean
    public Binding topicNotificationsBinding() {
        return BindingBuilder.bind(notificationsQueue())
                .to(topicExchange())
                .with("notifications.#");  // * - одно слово, # - ноль или более
    }
}
