package demo.rabbitmqdemo.service.blocks.batchretry;

public final class RetryPolicy {
    public static final int MAX_ATTEMPTS = 5;
    public static final long BACKOFF_DELAY_MS = 1000;
    public static final double BACKOFF_MULTIPLIER = 2.0;

    private RetryPolicy() {
    }
}
