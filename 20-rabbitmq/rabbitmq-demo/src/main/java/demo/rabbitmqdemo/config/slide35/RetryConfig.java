package demo.rabbitmqdemo.config.slide35;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.EnableRetry;

@Profile("slide35")
@EnableRetry
@Configuration
public class RetryConfig {

    @Bean
    public DirectExchange retryDlx() {
        return new DirectExchange("orders.retry.dlx");
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable("orders.retry.queue")
                .withArgument("x-dead-letter-exchange", "orders.retry.dlx")
                .withArgument("x-dead-letter-routing-key", "orders.retry.dlq")
                .build();
    }

    @Bean
    public Queue retryDlq() {
        return QueueBuilder.durable("orders.retry.dlq").build();
    }

    @Bean
    public Binding retryDlqBinding() {
        return BindingBuilder.bind(retryDlq())
                .to(retryDlx())
                .with("orders.retry.dlq");
    }

    @Bean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                              ObjectProvider<MessageConverter> messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        MessageConverter converter = messageConverter.getIfAvailable();
        if (converter != null) {
            factory.setMessageConverter(converter);
        }
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
