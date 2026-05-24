package demo.kafkademo.config;

import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerPartitionPausingBackOffManager;
import org.springframework.kafka.listener.ContainerPausingBackOffHandler;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.KafkaConsumerBackoffManager;
import org.springframework.kafka.listener.ListenerContainerPauseService;
import org.springframework.kafka.listener.ListenerContainerRegistry;
import org.springframework.kafka.listener.SeekUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class DelayedBackOffConfig {
    /**
     * Планировщик для снятия Kafka partition с паузы.
     * Важно: poolSize=1 достаточно для учебного стенда, а threadNamePrefix помогает увидеть
     * в логах, что resume partition выполняет отдельная backoff-инфраструктура.
     */
    @Bean
    TaskScheduler delayedBackOffTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("delayed-backoff-");
        scheduler.initialize();
        return scheduler;
    }

    /**
     * Manager для бизнес-задержки Messages With Delay.
     * Важно: registry нужен, чтобы найти listener container по id, а scheduler нужен,
     * чтобы снять с паузы только конкретную partition после наступления processAt.
     */
    @Bean
    KafkaConsumerBackoffManager delayedKafkaConsumerBackoffManager(
            ListenerContainerRegistry registry,
            TaskScheduler delayedBackOffTaskScheduler
    ) {
        // Инфраструктура Messages With Delay: планирует, когда снять partition с паузы.
        ListenerContainerPauseService pauseService =
                new ListenerContainerPauseService(registry, delayedBackOffTaskScheduler);
        // Этот manager использует DelayedRequestConsumer вместо Thread.sleep().
        return new ContainerPartitionPausingBackOffManager(
                registry,
                new ContainerPausingBackOffHandler(pauseService)
        );
    }

    /**
     * Отдельная listener factory только для orders.delayed.request.
     * Важно: configurer переносит стандартные Spring Boot настройки consumer-а,
     * а error handler заставляет Kafka перечитать текущий offset после resume partition.
     */
    @Bean
    ConcurrentKafkaListenerContainerFactory<Object, Object> delayedRequestKafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, consumerFactory);

        // KafkaBackoffException - штатный сигнал "еще рано".
        // Recoverer снова бросает backoff-exception, чтобы error handler сделал seek
        // на текущий offset. После resume partition Kafka отдаст эту же запись повторно.
        factory.setCommonErrorHandler(new DefaultErrorHandler(
                seekAgainOnKafkaBackoff(),
                new FixedBackOff(0L, 0L)
        ));
        return factory;
    }

    private ConsumerRecordRecoverer seekAgainOnKafkaBackoff() {
        return (record, exception) -> {
            if (SeekUtils.isBackoffException(exception)) {
                if (exception instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw new IllegalStateException(exception);
            }
        };
    }
}
