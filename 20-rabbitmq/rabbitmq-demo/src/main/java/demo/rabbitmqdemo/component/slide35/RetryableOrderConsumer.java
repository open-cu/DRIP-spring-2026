package demo.rabbitmqdemo.component.slide35;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;
import demo.rabbitmqdemo.service.slide35.RetryAttemptsProbe;

@Profile("slide35")
@Slf4j
@Component
public class RetryableOrderConsumer {

    private final RetryAttemptsProbe probe;

    public RetryableOrderConsumer(RetryAttemptsProbe probe) {
        this.probe = probe;
    }

    @RabbitListener(queues = "orders.retry.queue")
    @Retryable(
        value = Exception.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handleOrderWithRetry(Order order) {
        probe.increment();
        log.info("Processing order: {}", order);
        // Может выбросить исключение
        if (order.getId() == 999L) {
            throw new RuntimeException("Simulated failure");
        }
        // Успешная обработка
    }
}
