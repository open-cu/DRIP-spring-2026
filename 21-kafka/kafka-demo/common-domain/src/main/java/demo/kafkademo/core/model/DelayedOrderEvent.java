package demo.kafkademo.core.model;

import java.time.Instant;

public record DelayedOrderEvent(
        long orderId,
        String description,
        int delaySeconds,
        Instant createdAt,
        Instant processAt
) {
    public static DelayedOrderEvent of(long orderId, String description, int delaySeconds) {
        Instant createdAt = Instant.now();
        return new DelayedOrderEvent(orderId, description, delaySeconds, createdAt, createdAt.plusSeconds(delaySeconds));
    }
}
