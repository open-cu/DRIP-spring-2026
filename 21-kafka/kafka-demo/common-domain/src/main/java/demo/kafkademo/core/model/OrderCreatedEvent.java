package demo.kafkademo.core.model;

import java.time.Instant;

public record OrderCreatedEvent(
        long orderId,
        String description,
        boolean failForDemo,
        Instant createdAt
) {
    public static OrderCreatedEvent of(long orderId, String description) {
        return new OrderCreatedEvent(orderId, description, false, Instant.now());
    }

    public static OrderCreatedEvent failing(long orderId, String description) {
        return new OrderCreatedEvent(orderId, description, true, Instant.now());
    }
}
