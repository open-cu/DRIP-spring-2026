package demo.rabbitmqdemo.slides.slide27;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class Slide27TestOrdersTopologyConfig {

    @Bean
    public MessageConverter slide27TestJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
