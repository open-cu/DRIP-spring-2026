package demo.rabbitmqdemo.service.blocks.batchretry;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class RetryAttemptsProbe {

    private final AtomicInteger attempts = new AtomicInteger();

    public void increment() {
        attempts.incrementAndGet();
    }

    public int getAttempts() {
        return attempts.get();
    }

    public void reset() {
        attempts.set(0);
    }
}
