package demo.rabbitmqdemo.service.blocks.batchretry;

import java.util.concurrent.atomic.AtomicLong;

import demo.rabbitmqdemo.config.blocks.batchretry.BatchTopologyConfig;
import demo.rabbitmqdemo.config.blocks.batchretry.RetryTopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final BatchingRabbitTemplate batchingRabbitTemplate;
    private final RabbitTemplate retryRabbitTemplate;
    private final RetryAttemptsProbe retryAttemptsProbe;
    private final AtomicLong ids = new AtomicLong(5000);

    public MessagingService(
            @Qualifier("batchingRabbitTemplate") BatchingRabbitTemplate batchingRabbitTemplate,
            @Qualifier("retryRabbitTemplate") RabbitTemplate retryRabbitTemplate,
            RetryAttemptsProbe retryAttemptsProbe) {
        this.batchingRabbitTemplate = batchingRabbitTemplate;
        this.retryRabbitTemplate = retryRabbitTemplate;
        this.retryAttemptsProbe = retryAttemptsProbe;
    }

    public void publishBatch(int count) {
        if (count <= 0) {
            return;
        }
        for (int i = 1; i <= count; i++) {
            long id = ids.incrementAndGet();
            batchingRabbitTemplate.convertAndSend(
                    BatchTopologyConfig.BATCH_QUEUE,
                    new Order(id, "Пакетный заказ " + id)
            );
        }
    }

    public void publishRetry(long orderId, String description, boolean failForDemo) {
        if (orderId <= 0) {
            return;
        }
        String normalized = normalize(description);
        if (normalized == null) {
            return;
        }
        retryAttemptsProbe.reset();
        retryRabbitTemplate.convertAndSend(
                RetryTopologyConfig.RETRY_QUEUE,
                new Order(orderId, normalized, failForDemo)
        );
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
