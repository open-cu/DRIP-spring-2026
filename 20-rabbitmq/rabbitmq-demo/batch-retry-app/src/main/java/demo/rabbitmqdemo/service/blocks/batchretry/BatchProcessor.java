package demo.rabbitmqdemo.service.blocks.batchretry;

import java.util.List;

import demo.rabbitmqdemo.config.blocks.batchretry.BatchTopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.core.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BatchProcessor {

    private final OrderRepository repository;
    private final UiStateService uiStateService;

    public BatchProcessor(OrderRepository repository, UiStateService uiStateService) {
        this.repository = repository;
        this.uiStateService = uiStateService;
    }

    @RabbitListener(
            queues = BatchTopologyConfig.BATCH_QUEUE,
            containerFactory = "batchRabbitListenerContainerFactory"
    )
    public void handleBatch(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return;
        }
        repository.saveAll(orders);
        uiStateService.markBatchProcessed(orders.size());
        log.info("Processed batch of {} orders", orders.size());
    }
}
