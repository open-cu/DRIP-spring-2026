package demo.rabbitmqdemo.repository.slide34;

import demo.rabbitmqdemo.core.repository.OrderRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Profile("slide34")
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

