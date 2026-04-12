package demo.rabbitmqdemo.config.slide21;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;

@Profile("slide21")
@Configuration
public class RabbitTemplateDefaultsConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         @Nullable MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        if (messageConverter != null) {
            template.setMessageConverter(messageConverter);
        }
        template.setExchange("orders.direct");
        template.setRoutingKey("order.created");
        return template;
    }
}
