package demo.rabbitmqdemo.config.slide31;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide31")
@Configuration
public class DelayedMessageConfig {

    // DLX для переноса сообщений из wait queue в target
    @Bean
    public DirectExchange delayedExchange() {
        return new DirectExchange("delayed.exchange");
    }

    // Очередь, в которую Producer отправляет сообщения
    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable("delayed.queue")
                .withArgument("x-message-ttl", 30000) // TTL 30 секунд
                .withArgument("x-dead-letter-exchange", "delayed.exchange")
                .withArgument("x-dead-letter-routing-key", "process.order")
                .build();
    }

    @Bean
    public Queue delayed5sQueue() {
        return QueueBuilder.durable("delayed.5s.queue")
                .withArgument("x-message-ttl", 5_000)
                .withArgument("x-dead-letter-exchange", "delayed.exchange")
                .withArgument("x-dead-letter-routing-key", "process.order")
                .build();
    }

    @Bean
    public Queue delayed30sQueue() {
        return QueueBuilder.durable("delayed.30s.queue")
                .withArgument("x-message-ttl", 30_000)
                .withArgument("x-dead-letter-exchange", "delayed.exchange")
                .withArgument("x-dead-letter-routing-key", "process.order")
                .build();
    }

    @Bean
    public Queue delayed5mQueue() {
        return QueueBuilder.durable("delayed.5m.queue")
                .withArgument("x-message-ttl", 300_000)
                .withArgument("x-dead-letter-exchange", "delayed.exchange")
                .withArgument("x-dead-letter-routing-key", "process.order")
                .build();
    }

    // Целевая очередь, куда попадают сообщения после задержки
    @Bean
    public Queue targetQueue() {
        return QueueBuilder.durable("process.queue").build();
    }

    // Связываем target queue с DLX
    @Bean
    public Binding delayedBinding() {
        return BindingBuilder.bind(targetQueue())
                .to(delayedExchange())
                .with("process.order");
    }
}
