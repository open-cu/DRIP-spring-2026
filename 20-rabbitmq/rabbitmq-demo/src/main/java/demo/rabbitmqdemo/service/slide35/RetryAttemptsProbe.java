package demo.rabbitmqdemo.service.slide35;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Profile("slide35")
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

