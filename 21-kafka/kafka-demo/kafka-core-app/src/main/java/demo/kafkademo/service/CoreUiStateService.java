package demo.kafkademo.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import demo.kafkademo.core.model.Order;
import demo.kafkademo.core.model.OrderCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class CoreUiStateService {
    private final AtomicLong orderIds = new AtomicLong(1000);
    private final AtomicLong streamIds = new AtomicLong(5000);
    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final Map<Long, StreamView> streamResults = new ConcurrentHashMap<>();

    public OrderCreatedEvent registerOrder(String description) {
        long orderId = orderIds.incrementAndGet();
        String normalized = normalize(description, "заказ из core-блока");
        orders.put(orderId, Order.created(orderId, normalized));
        return OrderCreatedEvent.of(orderId, normalized);
    }

    public void markOrderProcessed(OrderCreatedEvent event) {
        orders.compute(event.orderId(), (id, existing) -> {
            Order current = existing == null ? Order.created(event.orderId(), event.description()) : existing;
            return current.processed();
        });
    }

    public StreamInput registerStreamInput(String description) {
        long streamId = streamIds.incrementAndGet();
        String normalized = normalize(description, "заказ для Kafka Streams");
        streamResults.put(streamId, new StreamView(streamId, normalized, "ожидает topology", Instant.now(), null));
        return new StreamInput(streamId, streamId + "|" + normalized);
    }

    public void markStreamResult(String payload) {
        String[] parts = payload.split("\\|", 3);
        if (parts.length < 3) {
            return;
        }
        long streamId = Long.parseLong(parts[0]);
        String result = parts[1] + ": " + parts[2];
        streamResults.compute(streamId, (id, existing) -> {
            String input = existing == null ? parts[2] : existing.input();
            Instant createdAt = existing == null ? Instant.now() : existing.createdAt();
            return new StreamView(id, input, result, createdAt, Instant.now());
        });
    }

    public PageData pageData() {
        List<OrderView> orderViews = orders.values().stream()
                .sorted(Comparator.comparing(Order::id).reversed())
                .map(order -> new OrderView(
                        order.id(),
                        order.description(),
                        order.status().label(),
                        order.createdAt(),
                        order.processedAt()
                ))
                .toList();
        List<StreamView> streamViews = streamResults.values().stream()
                .sorted(Comparator.comparing(StreamView::id).reversed())
                .toList();
        return new PageData(orderViews, streamViews);
    }

    public void reset() {
        orders.clear();
        streamResults.clear();
    }

    private String normalize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }

    public record StreamInput(long id, String payload) {
    }

    public record OrderView(long id, String description, String status, Instant createdAt, Instant processedAt) {
    }

    public record StreamView(long id, String input, String result, Instant createdAt, Instant processedAt) {
    }

    public record PageData(List<OrderView> orders, List<StreamView> streams) {
    }
}
