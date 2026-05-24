package demo.rabbitmqdemo.component.blocks.batchretry;

import demo.rabbitmqdemo.config.blocks.batchretry.RetryTopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.service.blocks.batchretry.RetryAttemptsProbe;
import demo.rabbitmqdemo.service.blocks.batchretry.RetryPolicy;
import demo.rabbitmqdemo.service.blocks.batchretry.UiStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RetryableOrderConsumer {

    private final RetryAttemptsProbe probe;
    private final UiStateService uiStateService;

    public RetryableOrderConsumer(RetryAttemptsProbe probe, UiStateService uiStateService) {
        this.probe = probe;
        this.uiStateService = uiStateService;
    }

    @RabbitListener(queues = RetryTopologyConfig.RETRY_QUEUE)
    @Retryable(
            maxAttempts = RetryPolicy.MAX_ATTEMPTS,
            backoff = @Backoff(delay = RetryPolicy.BACKOFF_DELAY_MS, multiplier = RetryPolicy.BACKOFF_MULTIPLIER)
    )
    public void handleOrderWithRetry(Order order) {
        probe.increment();
        uiStateService.markRetryAttempt(order);
        log.info("Обработка заказа: {}", order);
        if (order != null && order.failForDemo()) {
            throw new RuntimeException("Смоделированная ошибка");
        }
        uiStateService.markRetrySuccess(order);
    }

    @Recover
    public void recoverAfterRetry(Exception ex, Order order) {
        uiStateService.markRetryFailed(order);
        log.warn("Заказ {} завершился ошибкой после повторов: {}", order == null ? null : order.id(), ex.getMessage());
    }
}
