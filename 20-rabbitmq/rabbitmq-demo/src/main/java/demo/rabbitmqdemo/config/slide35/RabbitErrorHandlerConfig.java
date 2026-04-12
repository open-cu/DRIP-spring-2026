package demo.rabbitmqdemo.config.slide35;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.ErrorHandler;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;

@Profile("slide35")
@Slf4j
@Configuration
public class RabbitErrorHandlerConfig implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        log.error("Error in rabbit listener", t);
    }

    // Применение к listener
    @Bean(name = "rabbitErrorHandlerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitErrorHandlerContainerFactory(
            ConnectionFactory connectionFactory,
            ErrorHandler errorHandler) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(errorHandler);
        return factory;
    }
}
