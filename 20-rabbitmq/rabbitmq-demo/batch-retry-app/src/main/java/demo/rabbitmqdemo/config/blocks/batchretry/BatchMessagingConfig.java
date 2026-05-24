package demo.rabbitmqdemo.config.blocks.batchretry;

import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration(proxyBeanMethods = false)
public class BatchMessagingConfig {

    @Bean(name = "batchJsonConverter")
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public BatchingStrategy batchingStrategy(
            @Value("${demo.block.batch.producerSize:20}") int producerBatchSize,
            @Value("${demo.block.batch.producerBufferLimit:1048576}") int producerBufferLimit,
            @Value("${demo.block.batch.producerTimeoutMs:500}") long producerBatchTimeoutMs) {
        return new SimpleBatchingStrategy(producerBatchSize, producerBufferLimit, producerBatchTimeoutMs);
    }

    @Bean
    public TaskScheduler batchScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("batch-");
        return scheduler;
    }

    @Bean(name = "batchingRabbitTemplate")
    public BatchingRabbitTemplate batchingRabbitTemplate(ConnectionFactory connectionFactory,
                                                         BatchingStrategy batchingStrategy,
                                                         TaskScheduler batchScheduler,
                                                         @Qualifier("batchJsonConverter") MessageConverter jsonConverter) {
        BatchingRabbitTemplate template =
                new BatchingRabbitTemplate(connectionFactory, batchingStrategy, batchScheduler);
        template.setMessageConverter(jsonConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory batchRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            @Qualifier("batchJsonConverter") MessageConverter jsonConverter,
            @Value("${demo.block.batch.consumerSize:20}") int consumerBatchSize,
            @Value("${demo.block.batch.consumerReceiveTimeoutMs:1000}") long consumerReceiveTimeoutMs) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonConverter);
        factory.setBatchListener(true);
        factory.setConsumerBatchEnabled(true);
        factory.setBatchSize(consumerBatchSize);
        factory.setReceiveTimeout(consumerReceiveTimeoutMs);
        return factory;
    }
}
