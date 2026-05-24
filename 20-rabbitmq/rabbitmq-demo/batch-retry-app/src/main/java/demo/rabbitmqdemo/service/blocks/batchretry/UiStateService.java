package demo.rabbitmqdemo.service.blocks.batchretry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import demo.rabbitmqdemo.core.model.Order;
import org.springframework.stereotype.Service;

@Service
public class UiStateService {

    public static final String STATUS_RETRY_ATTEMPT = "ПОВТОРНАЯ_ПОПЫТКА";
    public static final String STATUS_SUCCESS = "УСПЕХ";
    public static final String STATUS_FAILED_AFTER_RETRIES = "ОШИБКА_ПОСЛЕ_ПОВТОРОВ";
    public static final String STATUS_NOT_FOUND = "НЕ_НАЙДЕН";

    private final AtomicInteger batchProcessedCount = new AtomicInteger();
    private final AtomicInteger batchSaveCalls = new AtomicInteger();
    private final Map<Long, String> retryStatusByOrderId = new ConcurrentHashMap<>();

    public void markBatchProcessed(int size) {
        batchSaveCalls.incrementAndGet();
        batchProcessedCount.addAndGet(size);
    }

    public void markRetryAttempt(Order order) {
        if (order != null && order.id() != null) {
            retryStatusByOrderId.put(order.id(), STATUS_RETRY_ATTEMPT);
        }
    }

    public void markRetrySuccess(Order order) {
        if (order != null && order.id() != null) {
            retryStatusByOrderId.put(order.id(), STATUS_SUCCESS);
        }
    }

    public void markRetryFailed(Order order) {
        if (order != null && order.id() != null) {
            retryStatusByOrderId.put(order.id(), STATUS_FAILED_AFTER_RETRIES);
        }
    }

    public int batchProcessedCount() {
        return batchProcessedCount.get();
    }

    public int batchSaveCalls() {
        return batchSaveCalls.get();
    }

    public String retryStatus(Long orderId) {
        if (orderId == null) {
            return "-";
        }
        return retryStatusByOrderId.getOrDefault(orderId, STATUS_NOT_FOUND);
    }

    public void reset() {
        batchProcessedCount.set(0);
        batchSaveCalls.set(0);
        retryStatusByOrderId.clear();
    }
}
