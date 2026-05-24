package demo.rabbitmqdemo.service.blocks.batchretry;

import demo.rabbitmqdemo.config.blocks.batchretry.BatchTopologyConfig;
import demo.rabbitmqdemo.config.blocks.batchretry.RetryTopologyConfig;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {
    public static final int RETRY_MAX_ATTEMPTS = RetryPolicy.MAX_ATTEMPTS;
    public static final long RETRY_BACKOFF_DELAY_MS = RetryPolicy.BACKOFF_DELAY_MS;
    public static final double RETRY_BACKOFF_MULTIPLIER = RetryPolicy.BACKOFF_MULTIPLIER;

    private final MessagingService messagingService;
    private final RetryAttemptsProbe retryAttemptsProbe;
    private final UiStateService uiStateService;
    private final RabbitAdmin rabbitAdmin;
    private final int producerBatchSize;
    private final int producerBufferLimit;
    private final long producerTimeoutMs;
    private final int consumerBatchSize;
    private final long consumerReceiveTimeoutMs;

    public ScenarioService(
            MessagingService messagingService,
            RetryAttemptsProbe retryAttemptsProbe,
            UiStateService uiStateService,
            RabbitAdmin rabbitAdmin,
            @Value("${demo.block.batch.producerSize:20}") int producerBatchSize,
            @Value("${demo.block.batch.producerBufferLimit:1048576}") int producerBufferLimit,
            @Value("${demo.block.batch.producerTimeoutMs:500}") long producerTimeoutMs,
            @Value("${demo.block.batch.consumerSize:20}") int consumerBatchSize,
            @Value("${demo.block.batch.consumerReceiveTimeoutMs:1000}") long consumerReceiveTimeoutMs
    ) {
        this.messagingService = messagingService;
        this.retryAttemptsProbe = retryAttemptsProbe;
        this.uiStateService = uiStateService;
        this.rabbitAdmin = rabbitAdmin;
        this.producerBatchSize = producerBatchSize;
        this.producerBufferLimit = producerBufferLimit;
        this.producerTimeoutMs = producerTimeoutMs;
        this.consumerBatchSize = consumerBatchSize;
        this.consumerReceiveTimeoutMs = consumerReceiveTimeoutMs;
    }

    public PageData pageData(Long orderId) {
        return new PageData(
                uiStateService.batchSaveCalls(),
                uiStateService.batchProcessedCount(),
                producerBatchSize,
                producerBufferLimit,
                producerTimeoutMs,
                consumerBatchSize,
                consumerReceiveTimeoutMs,
                retryAttemptsProbe.getAttempts(),
                RETRY_MAX_ATTEMPTS,
                RETRY_BACKOFF_DELAY_MS,
                RETRY_BACKOFF_MULTIPLIER,
                orderId,
                uiStateService.retryStatus(orderId)
        );
    }

    public void publishBatch(int count) {
        messagingService.publishBatch(count);
    }

    public void publishRetry(long orderId, String description, boolean failForDemo) {
        messagingService.publishRetry(orderId, description, failForDemo);
    }

    public void reset() {
        uiStateService.reset();
        retryAttemptsProbe.reset();
        purgeIfExists(BatchTopologyConfig.BATCH_QUEUE);
        purgeIfExists(RetryTopologyConfig.RETRY_QUEUE);
        purgeIfExists(RetryTopologyConfig.RETRY_DLQ);
    }

    private void purgeIfExists(String queueName) {
        try {
            if (rabbitAdmin.getQueueInfo(queueName) == null) {
                return;
            }
            rabbitAdmin.purgeQueue(queueName, false);
        } catch (Exception ignored) {
            // In demo mode reset should be resilient even if topology is absent or recreating.
        }
    }

    public record PageData(
            int batchSaveCalls,
            int batchProcessedCount,
            int producerBatchSize,
            int producerBufferLimit,
            long producerTimeoutMs,
            int consumerBatchSize,
            long consumerReceiveTimeoutMs,
            int retryAttempts,
            int retryMaxAttempts,
            long retryBackoffDelayMs,
            double retryBackoffMultiplier,
            Long selectedOrderId,
            String retryStatus
    ) {
    }
}
