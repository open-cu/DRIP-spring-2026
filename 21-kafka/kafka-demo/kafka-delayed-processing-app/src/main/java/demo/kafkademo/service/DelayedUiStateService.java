package demo.kafkademo.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import demo.kafkademo.core.model.DelayedOrderEvent;
import demo.kafkademo.core.model.Order;
import org.springframework.stereotype.Service;

@Service
public class DelayedUiStateService {
    private static final Set<Integer> ALLOWED_DELAYS = Set.of(5, 30, 300);

    private final AtomicLong ids = new AtomicLong(3000);
    private final Map<Long, DelayedOrderState> orders = new ConcurrentHashMap<>();

    public DelayedOrderEvent registerOrder(String description, int requestedDelaySeconds) {
        int delaySeconds = normalizeDelay(requestedDelaySeconds);
        long orderId = ids.incrementAndGet();
        String normalized = normalize(description, "отложенный заказ");
        Instant createdAt = Instant.now();
        Instant dueAt = createdAt.plusSeconds(delaySeconds);
        orders.put(orderId, new DelayedOrderState(
                Order.pending(orderId, normalized),
                delaySeconds,
                dueAt,
                "событие ушло в request topic"
        ));
        return new DelayedOrderEvent(orderId, normalized, delaySeconds, createdAt, dueAt);
    }

    public void markDelayedRead(DelayedOrderEvent event, Instant processAt) {
        orders.computeIfPresent(event.orderId(), (id, current) -> new DelayedOrderState(
                current.order(),
                current.delaySeconds(),
                current.dueAt(),
                "событие прочитано раньше срока, partition на паузе до " + processAt
        ));
    }

    public void markReleased(DelayedOrderEvent event) {
        orders.computeIfPresent(event.orderId(), (id, current) -> new DelayedOrderState(
                current.order(),
                current.delaySeconds(),
                current.dueAt(),
                "время пришло, событие ушло в process topic"
        ));
    }

    public void markProcessed(DelayedOrderEvent event) {
        orders.compute(event.orderId(), (id, current) -> {
            DelayedOrderState base = current == null ? fromEvent(event) : current;
            return new DelayedOrderState(base.order().processed(), base.delaySeconds(), base.dueAt(), "обработан из process topic");
        });
    }

    public void markFailed(DelayedOrderEvent event) {
        orders.compute(event.orderId(), (id, current) -> {
            DelayedOrderState base = current == null ? fromEvent(event) : current;
            return new DelayedOrderState(base.order().failed(), base.delaySeconds(), base.dueAt(), "ошибка обработки");
        });
    }

    public void markRetryAttempt(DelayedOrderEvent event, int attempt) {
        orders.compute(event.orderId(), (id, current) -> {
            DelayedOrderState base = current == null ? fromEvent(event) : current;
            return new DelayedOrderState(
                    base.order().failed(),
                    base.delaySeconds(),
                    base.dueAt(),
                    "ошибка обработки, retry attempt " + attempt
            );
        });
    }

    public void markSentToDlt(DelayedOrderEvent event) {
        orders.compute(event.orderId(), (id, current) -> {
            DelayedOrderState base = current == null ? fromEvent(event) : current;
            return new DelayedOrderState(base.order().sentToDlt(), base.delaySeconds(), base.dueAt(), "сообщение в DLT");
        });
    }

    public PageData pageData() {
        Instant now = Instant.now();
        List<OrderView> views = orders.values().stream()
                .sorted(Comparator.comparing(state -> state.order().id(), Comparator.reverseOrder()))
                .map(state -> toView(state, now))
                .toList();
        return new PageData(views, List.of(5, 30, 300));
    }

    public void reset() {
        orders.clear();
    }

    private DelayedOrderState fromEvent(DelayedOrderEvent event) {
        Instant dueAt = event.processAt();
        return new DelayedOrderState(Order.pending(event.orderId(), event.description()), event.delaySeconds(), dueAt, "восстановлен из события");
    }

    private OrderView toView(DelayedOrderState state, Instant now) {
        Duration total = Duration.ofSeconds(state.delaySeconds());
        Duration elapsed = Duration.between(state.order().createdAt(), now);
        int progress = (int) Math.min(100, Math.max(0, elapsed.toMillis() * 100 / Math.max(1, total.toMillis())));
        if (state.order().processedAt() != null) {
            progress = 100;
        }
        return new OrderView(
                state.order().id(),
                state.order().description(),
                state.order().status().label(),
                state.delaySeconds(),
                state.dueAt(),
                progress,
                state.note(),
                state.order().processedAt()
        );
    }

    private int normalizeDelay(int requestedDelaySeconds) {
        if (ALLOWED_DELAYS.contains(requestedDelaySeconds)) {
            return requestedDelaySeconds;
        }
        return 5;
    }

    private String normalize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }

    private record DelayedOrderState(Order order, int delaySeconds, Instant dueAt, String note) {
    }

    public record OrderView(
            long id,
            String description,
            String status,
            int delaySeconds,
            Instant dueAt,
            int progressPercent,
            String note,
            Instant processedAt
    ) {
    }

    public record PageData(List<OrderView> orders, List<Integer> delays) {
    }
}
