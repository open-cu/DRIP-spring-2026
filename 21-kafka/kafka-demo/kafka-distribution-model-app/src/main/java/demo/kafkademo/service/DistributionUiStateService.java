package demo.kafkademo.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import demo.kafkademo.core.model.DistributionEvent;
import org.springframework.stereotype.Service;

@Service
public class DistributionUiStateService {
    private final AtomicLong ids = new AtomicLong(2000);
    private final Map<Long, EventView> events = new ConcurrentHashMap<>();

    public DistributionEvent createEvent(String key, String payload) {
        long id = ids.incrementAndGet();
        return DistributionEvent.of(id, normalize(key, "order-42"), normalize(payload, "событие заказа"));
    }

    public void markReceived(DistributionEvent event, int partition, long offset, String consumerName) {
        events.put(event.id(), new EventView(
                event.id(),
                event.key(),
                event.payload(),
                partition,
                offset,
                consumerName,
                event.createdAt(),
                Instant.now()
        ));
    }

    public PageData pageData() {
        List<EventView> views = events.values().stream()
                .sorted(Comparator.comparing(EventView::id).reversed())
                .toList();
        return new PageData(views, 3, "orders.distribution");
    }

    public void reset() {
        events.clear();
    }

    private String normalize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }

    public record EventView(
            long id,
            String eventKey,
            String payload,
            int partition,
            long offset,
            String consumerName,
            Instant createdAt,
            Instant receivedAt
    ) {
    }

    public record PageData(List<EventView> events, int partitions, String topic) {
    }
}
