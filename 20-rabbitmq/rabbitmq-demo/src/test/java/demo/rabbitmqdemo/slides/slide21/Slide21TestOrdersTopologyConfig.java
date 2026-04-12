package demo.rabbitmqdemo.slides.slide21;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class Slide21TestOrdersTopologyConfig {

    @Bean
    public MessageConverter slide21TestJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
