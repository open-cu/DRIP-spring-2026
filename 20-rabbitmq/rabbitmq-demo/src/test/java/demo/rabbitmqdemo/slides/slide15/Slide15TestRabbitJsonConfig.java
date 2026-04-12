package demo.rabbitmqdemo.slides.slide15;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class Slide15TestRabbitJsonConfig {

    @Bean
    public MessageConverter slide15TestJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
