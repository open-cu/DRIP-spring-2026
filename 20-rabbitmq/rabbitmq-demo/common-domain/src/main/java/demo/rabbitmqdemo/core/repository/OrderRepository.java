package demo.rabbitmqdemo.core.repository;

public interface OrderRepository {
    void saveAll(Iterable<?> items);
}

