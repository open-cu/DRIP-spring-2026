package demo.rabbitmqdemo.service.blocks.springcore;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.core.model.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public class UiStateService {

    public static final String STATUS_PENDING = "ОЖИДАЕТ";
    public static final String STATUS_PROCESSED = "ОБРАБОТАН";
    public static final String STATUS_UNKNOWN = "НЕИЗВЕСТНО";
    public static final String DESCRIPTION_RPC_ONLY = "ТОЛЬКО_RPC";
    private static final String RPC_NOT_REQUESTED = "-";

    private final AtomicLong ids = new AtomicLong(2000);
    private final Map<Long, OrderView> orders = new ConcurrentHashMap<>();
    private final AtomicLong textIds = new AtomicLong(7000);
    private final Map<Long, TextView> textMessages = new ConcurrentHashMap<>();

    public Order createOrder(String description) {
        long id = ids.incrementAndGet();
        orders.put(id, new OrderView(id, description, STATUS_PENDING, RPC_NOT_REQUESTED));
        return new Order(id, description);
    }

    public void markProcessed(Order order) {
        if (order == null || order.id() == null) {
            return;
        }
        orders.compute(order.id(), (id, existing) -> {
            if (existing == null) {
                return new OrderView(id, order.description(), STATUS_PROCESSED, RPC_NOT_REQUESTED);
            }
            return new OrderView(id, existing.description(), STATUS_PROCESSED, existing.rpcStatus());
        });
    }

    public void markRpcResult(OrderResponse response) {
        if (response == null || response.id() == null) {
            return;
        }
        orders.compute(response.id(), (id, existing) -> {
            if (existing == null) {
                return new OrderView(id, DESCRIPTION_RPC_ONLY, STATUS_UNKNOWN, response.status());
            }
            return new OrderView(id, existing.description(), existing.status(), response.status());
        });
    }

    public List<OrderView> listOrders() {
        return orders.values().stream()
                .sorted(Comparator.comparingLong(OrderView::id).reversed())
                .toList();
    }

    public void registerSimpleText(String text) {
        long id = textIds.incrementAndGet();
        textMessages.put(id, new TextView(id, text, STATUS_PENDING));
    }

    public void markSimpleTextProcessed(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        textMessages.entrySet().stream()
                .filter(entry -> STATUS_PENDING.equals(entry.getValue().status()) && text.equals(entry.getValue().text()))
                .findFirst()
                .ifPresent(entry -> textMessages.put(
                        entry.getKey(),
                        new TextView(entry.getValue().id(), entry.getValue().text(), STATUS_PROCESSED)
                ));
    }

    public List<TextView> listTextMessages() {
        return textMessages.values().stream()
                .sorted(Comparator.comparingLong(TextView::id).reversed())
                .toList();
    }

    public record OrderView(Long id, String description, String status, String rpcStatus) {
    }

    public record TextView(Long id, String text, String status) {
    }
}
