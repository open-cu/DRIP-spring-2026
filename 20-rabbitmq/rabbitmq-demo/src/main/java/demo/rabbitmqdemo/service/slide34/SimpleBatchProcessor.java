package demo.rabbitmqdemo.service.slide34;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.core.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Profile("slide34")
@Slf4j
@Service
@ConditionalOnProperty(name = "demo.slide34.mode", havingValue = "simple", matchIfMissing = true)
public class SimpleBatchProcessor {

    private final OrderRepository repository;
    private final int batchSize;
    private final List<Order> buffer = new ArrayList<>();
    private final Object lock = new Object();

    public SimpleBatchProcessor(OrderRepository repository,
                                @Value("${demo.slide34.simple.batchSize:100}") int batchSize) {
        this.repository = repository;
        this.batchSize = batchSize;
    }

    @RabbitListener(queues = "batch.queue")
    public void handleOrder(Order order) {
        synchronized (lock) {
            buffer.add(order);

            if (buffer.size() >= batchSize) {
                flush();
            }
        }
    }

    @Scheduled(fixedDelayString = "${demo.slide34.simple.flushDelayMs:5000}")
    public void flushByTimeout() {
        synchronized (lock) {
            if (buffer.isEmpty()) {
                return;
            }
        }
        flush();
    }

    private void flush() {
        List<Order> batch;
        synchronized (lock) {
            if (buffer.isEmpty()) {
                return;
            }
            batch = new ArrayList<>(buffer);
            buffer.clear();
        }

        // Пакетная запись в БД
        repository.saveAll(batch);
        log.info("Processed batch of {} orders", batch.size());
    }
}
