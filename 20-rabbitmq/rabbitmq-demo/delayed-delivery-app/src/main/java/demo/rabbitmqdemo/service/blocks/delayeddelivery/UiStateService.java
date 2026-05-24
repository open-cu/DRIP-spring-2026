package demo.rabbitmqdemo.service.blocks.delayeddelivery;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import demo.rabbitmqdemo.core.model.Order;
import org.springframework.stereotype.Service;

@Service
public class UiStateService {

    public static final String STATUS_PENDING = "ОЖИДАЕТ";
    public static final String STATUS_PROCESSED = "ОБРАБОТАН";

    private final AtomicLong ids = new AtomicLong(4000);
    private final Map<Long, OrderView> orders = new ConcurrentHashMap<>();

    public Order createPendingOrder(String description, int delaySeconds) {
        long id = ids.incrementAndGet();
        orders.put(id, new OrderView(
                id,
                description,
                delaySeconds,
                STATUS_PENDING,
                Instant.now().toString(),
                "-"
        ));
        return new Order(id, description);
    }

    public void markProcessed(Order order) {
        if (order == null || order.id() == null) {
            return;
        }
        orders.compute(order.id(), (id, existing) -> {
            if (existing == null) {
                return new OrderView(id, order.description(), 0, STATUS_PROCESSED, "-", Instant.now().toString());
            }
            return new OrderView(
                    existing.id(),
                    existing.description(),
                    existing.delaySeconds(),
                    STATUS_PROCESSED,
                    existing.createdAt(),
                    Instant.now().toString()
            );
        });
    }

    public List<OrderView> listOrders() {
        return orders.values().stream()
                .sorted(Comparator.comparingLong(OrderView::id).reversed())
                .toList();
    }

    public void reset() {
        orders.clear();
    }

    public record OrderView(
            Long id,
            String description,
            int delaySeconds,
            String status,
            String createdAt,
            String processedAt
    ) {
    }
}
