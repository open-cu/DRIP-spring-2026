package demo.rabbitmqdemo.slides.slide24;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class Slide24TestOrdersTopologyConfig {

    @Bean
    public MessageConverter slide24TestJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
