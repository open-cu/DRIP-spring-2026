package demo.rabbitmqdemo.config.blocks.routingbasics;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutExchangeConfig {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("notifications.fanout");
    }

    @Bean
    public Queue notificationMoscowQueue() {
            return QueueBuilder.durable("notifications.moscow.queue").build();
    }
    @Bean
    public Queue notificationPscovQueue() {
        return QueueBuilder.durable("notifications.pscov.queue").build();
    }
    @Bean
    public Queue notificationTverQueue() {
        return QueueBuilder.durable("notifications.tver.queue").build();
    }
    @Bean
    public Queue notificationElistaQueue() {
        return QueueBuilder.durable("notifications.elista.queue").build();
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(notificationMoscowQueue())
                .to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(notificationPscovQueue())
                .to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding3() {
        return BindingBuilder.bind(notificationTverQueue())
                .to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding4() {
        return BindingBuilder.bind(notificationElistaQueue())
                .to(fanoutExchange());
    }
}
