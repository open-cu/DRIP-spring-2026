package demo.rabbitmqdemo.config.slide34;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide34")
@Configuration(proxyBeanMethods = false)
public class Slide34RabbitJsonConfig {

    @Bean
    public MessageConverter slide34JsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
