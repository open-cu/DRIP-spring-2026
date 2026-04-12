package demo.rabbitmqdemo.config.slide35;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide35")
@Configuration(proxyBeanMethods = false)
public class Slide35RabbitJsonConfig {

    @Bean
    public MessageConverter slide35JsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
