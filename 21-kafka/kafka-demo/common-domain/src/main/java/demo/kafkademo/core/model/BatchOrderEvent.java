package demo.kafkademo.core.model;

import java.time.Instant;

public record BatchOrderEvent(
        long orderId,
        String description,
        boolean failForDemo,
        Instant createdAt
) {
    public static BatchOrderEvent ok(long orderId, String description) {
        return new BatchOrderEvent(orderId, description, false, Instant.now());
    }

    public static BatchOrderEvent failing(long orderId, String description) {
        return new BatchOrderEvent(orderId, description, true, Instant.now());
    }
}
