package demo.rabbitmqdemo.config.slide25;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide25")
@Configuration(proxyBeanMethods = false)
public class Slide25RabbitJsonConfig {

    @Bean
    public MessageConverter slide25JsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
