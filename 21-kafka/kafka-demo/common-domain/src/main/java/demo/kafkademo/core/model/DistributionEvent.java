package demo.kafkademo.core.model;

import java.time.Instant;

public record DistributionEvent(
        long id,
        String key,
        String payload,
        Instant createdAt
) {
    public static DistributionEvent of(long id, String key, String payload) {
        return new DistributionEvent(id, key, payload, Instant.now());
    }
}
