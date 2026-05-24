package demo.rabbitmqdemo.config.blocks.routingbasics;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeConfig {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("common.topic");
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable("notifications.queue").build();
    }

    @Bean
    public Binding topicOrderBinding(Queue orderQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(orderQueue)
                .to(topicExchange)
                .with("order.*");  // * - одно слово, # - ноль или более
    }

    @Bean
    public Binding topicNotificationsBinding(Queue notificationsQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(notificationsQueue)
                .to(topicExchange)
                .with("notifications.#");  // * - одно слово, # - ноль или более
    }
}
