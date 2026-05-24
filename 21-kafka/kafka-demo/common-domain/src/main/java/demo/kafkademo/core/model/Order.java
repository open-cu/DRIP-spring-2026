package demo.kafkademo.core.model;

import java.time.Instant;

public record Order(
        long id,
        String description,
        OrderStatus status,
        Instant createdAt,
        Instant processedAt
) {
    public static Order created(long id, String description) {
        return new Order(id, description, OrderStatus.CREATED, Instant.now(), null);
    }

    public static Order pending(long id, String description) {
        return new Order(id, description, OrderStatus.PENDING, Instant.now(), null);
    }

    public Order processed() {
        return new Order(id, description, OrderStatus.PROCESSED, createdAt, Instant.now());
    }

    public Order failed() {
        return new Order(id, description, OrderStatus.FAILED, createdAt, Instant.now());
    }

    public Order sentToDlt() {
        return new Order(id, description, OrderStatus.SENT_TO_DLT, createdAt, Instant.now());
    }
}
