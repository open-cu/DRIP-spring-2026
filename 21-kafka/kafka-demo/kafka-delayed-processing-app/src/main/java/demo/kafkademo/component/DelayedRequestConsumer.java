package demo.kafkademo.component;

import java.time.Instant;

import demo.kafkademo.service.DelayedMessageHeaders;
import demo.kafkademo.config.KafkaTopicsConfig;
import demo.kafkademo.core.model.DelayedOrderEvent;
import demo.kafkademo.service.DelayedUiStateService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaConsumerBackoffManager;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class DelayedRequestConsumer {
    private static final String LISTENER_ID = "delayedRequestListener";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConsumerBackoffManager backOffManager;
    private final DelayedUiStateService stateService;

    public DelayedRequestConsumer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Qualifier("delayedKafkaConsumerBackoffManager")
            KafkaConsumerBackoffManager backOffManager,
            DelayedUiStateService stateService
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.backOffManager = backOffManager;
        this.stateService = stateService;
    }

    @KafkaListener(
            id = LISTENER_ID,
            topics = KafkaTopicsConfig.DELAYED_REQUEST,
            groupId = "kafka-delayed-request-ui",
            containerFactory = "delayedRequestKafkaListenerContainerFactory"
    )
    void handle(
            ConsumerRecord<String, DelayedOrderEvent> record,
            Acknowledgment acknowledgment,
            Consumer<?, ?> consumer
    ) {
        DelayedOrderEvent event = record.value();
        // Главное решение по задержке: processAt - время обработки, а не retry-интервал.
        Instant processAt = DelayedMessageHeaders.processAt(record.headers()).orElse(event.processAt());
        if (Instant.now().isBefore(processAt)) {
            stateService.markDelayedRead(event, processAt);
            // Пока не подтверждаем запись: после истечения задержки Kafka снова отдаст ее consumer-у.
            pausePartitionUntil(record, consumer, processAt);
            return;
        }

        // Задержка закончилась: событие можно передать в обычную обработку и подтвердить request-запись.
        kafkaTemplate.send(KafkaTopicsConfig.DELAYED_PROCESS, String.valueOf(event.orderId()), event).join();
        stateService.markReleased(event);
        acknowledgment.acknowledge();
    }

    private void pausePartitionUntil(
            ConsumerRecord<String, DelayedOrderEvent> record,
            Consumer<?, ?> consumer,
            Instant processAt
    ) {
        TopicPartition topicPartition =
                new TopicPartition(record.topic(), record.partition());
        // Инфраструктура Messages With Delay:
        // Spring Kafka ставит на паузу только эту partition до processAt.
        KafkaConsumerBackoffManager.Context context = backOffManager.createContext(
                processAt.toEpochMilli(),
                LISTENER_ID,
                topicPartition,
                consumer
        );
        backOffManager.backOffIfNecessary(context);
    }
}
