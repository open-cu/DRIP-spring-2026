package demo.kafkademo.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import demo.kafkademo.core.model.BatchOrderEvent;
import demo.kafkademo.core.model.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class BatchUiStateService {
    private final AtomicLong ids = new AtomicLong(4000);
    private final AtomicInteger published = new AtomicInteger();
    private final AtomicInteger processed = new AtomicInteger();
    private final AtomicInteger failed = new AtomicInteger();
    private final AtomicInteger sentToDlt = new AtomicInteger();
    private final AtomicInteger lastBatchSize = new AtomicInteger();
    private final Map<Long, BatchOrderView> orders = new ConcurrentHashMap<>();

    public BatchOrderEvent createOkEvent(String description) {
        long id = ids.incrementAndGet();
        String normalized = normalize(description, "batch заказ " + id);
        published.incrementAndGet();
        orders.put(id, new BatchOrderView(id, normalized, OrderStatus.CREATED.label(), false, null, Instant.now(), null));
        return BatchOrderEvent.ok(id, normalized);
    }

    public BatchOrderEvent createFailingEvent(String description) {
        long id = ids.incrementAndGet();
        String normalized = normalize(description, "batch заказ с ошибкой " + id);
        published.incrementAndGet();
        orders.put(id, new BatchOrderView(id, normalized, OrderStatus.CREATED.label(), true, null, Instant.now(), null));
        return BatchOrderEvent.failing(id, normalized);
    }

    public void markBatchReceived(int size) {
        lastBatchSize.set(size);
    }

    public void markProcessed(BatchOrderEvent event, String note) {
        processed.incrementAndGet();
        orders.put(event.orderId(), new BatchOrderView(
                event.orderId(),
                event.description(),
                OrderStatus.PROCESSED.label(),
                event.failForDemo(),
                note,
                event.createdAt(),
                Instant.now()
        ));
    }

    public void markFailed(BatchOrderEvent event, String note) {
        failed.incrementAndGet();
        orders.put(event.orderId(), new BatchOrderView(
                event.orderId(),
                event.description(),
                OrderStatus.FAILED.label(),
                event.failForDemo(),
                note,
                event.createdAt(),
                Instant.now()
        ));
    }

    public void markDlt(BatchOrderEvent event) {
        sentToDlt.incrementAndGet();
        orders.put(event.orderId(), new BatchOrderView(
                event.orderId(),
                event.description(),
                OrderStatus.SENT_TO_DLT.label(),
                event.failForDemo(),
                "после ошибки отправлено в DLT",
                event.createdAt(),
                Instant.now()
        ));
    }

    public PageData pageData() {
        List<BatchOrderView> views = orders.values().stream()
                .sorted(Comparator.comparing(BatchOrderView::id).reversed())
                .toList();
        return new PageData(
                views,
                published.get(),
                processed.get(),
                failed.get(),
                sentToDlt.get(),
                lastBatchSize.get()
        );
    }

    public void reset() {
        published.set(0);
        processed.set(0);
        failed.set(0);
        sentToDlt.set(0);
        lastBatchSize.set(0);
        orders.clear();
    }

    private String normalize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }

    public record BatchOrderView(
            long id,
            String description,
            String status,
            boolean failForDemo,
            String note,
            Instant createdAt,
            Instant processedAt
    ) {
    }

    public record PageData(
            List<BatchOrderView> orders,
            int published,
            int processed,
            int failed,
            int sentToDlt,
            int lastBatchSize
    ) {
    }
}
