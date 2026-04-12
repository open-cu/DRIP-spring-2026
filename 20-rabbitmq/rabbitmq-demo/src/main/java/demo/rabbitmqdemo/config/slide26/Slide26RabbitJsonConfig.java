package demo.rabbitmqdemo.config.slide26;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide26")
@Configuration(proxyBeanMethods = false)
public class Slide26RabbitJsonConfig {

    @Bean
    public MessageConverter slide26JsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
