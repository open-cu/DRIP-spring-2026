package demo.rabbitmqdemo.repository.blocks.batchretry;

import java.util.concurrent.atomic.AtomicInteger;

import demo.rabbitmqdemo.core.repository.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final AtomicInteger saveCalls = new AtomicInteger();
    private final AtomicInteger totalItemsSaved = new AtomicInteger();
    private final AtomicInteger lastBatchSize = new AtomicInteger();

    @Override
    public void saveAll(Iterable<?> items) {
        int size = 0;
        for (Object ignored : items) {
            size++;
        }
        saveCalls.incrementAndGet();
        totalItemsSaved.addAndGet(size);
        lastBatchSize.set(size);
    }

    public int getSaveCalls() {
        return saveCalls.get();
    }

    public int getTotalItemsSaved() {
        return totalItemsSaved.get();
    }

    public int getLastBatchSize() {
        return lastBatchSize.get();
    }

    public void reset() {
        saveCalls.set(0);
        totalItemsSaved.set(0);
        lastBatchSize.set(0);
    }
}
