package demo.rabbitmqdemo.config.blocks.delayeddelivery;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MessagingConfig {

    @Bean
    public MessageConverter delayedDeliveryJsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
