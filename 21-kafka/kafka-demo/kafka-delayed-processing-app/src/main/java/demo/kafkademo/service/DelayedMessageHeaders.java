package demo.kafkademo.service;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Optional;

import org.apache.kafka.common.header.Headers;

public final class DelayedMessageHeaders {
    // Тема Messages With Delay: бизнес-время "обрабатывать событие не раньше этого момента".
    public static final String PROCESS_AT = "process-at";

    private DelayedMessageHeaders() {
    }

    public static byte[] processAtValue(Instant processAt) {
        return ByteBuffer.allocate(Long.BYTES)
                .putLong(processAt.toEpochMilli())
                .array();
    }

    public static Optional<Instant> processAt(Headers headers) {
        var header = headers.lastHeader(PROCESS_AT);
        if (header == null || header.value() == null || header.value().length != Long.BYTES) {
            return Optional.empty();
        }
        return Optional.of(Instant.ofEpochMilli(ByteBuffer.wrap(header.value()).getLong()));
    }
}
