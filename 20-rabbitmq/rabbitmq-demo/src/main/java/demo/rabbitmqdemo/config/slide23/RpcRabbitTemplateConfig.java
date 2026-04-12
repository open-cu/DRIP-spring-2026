package demo.rabbitmqdemo.config.slide23;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.lang.Nullable;

@Profile("slide23")
@Configuration
public class RpcRabbitTemplateConfig {

    @Bean
    public MessageConverter slide23JsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         @Nullable MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        if (messageConverter != null) {
            rabbitTemplate.setMessageConverter(messageConverter);
        }
        return rabbitTemplate;
    }
}
