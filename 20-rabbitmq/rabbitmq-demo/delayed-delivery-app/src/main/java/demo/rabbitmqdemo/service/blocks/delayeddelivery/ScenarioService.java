package demo.rabbitmqdemo.service.blocks.delayeddelivery;

import java.util.List;
import java.util.Set;

import demo.rabbitmqdemo.config.blocks.delayeddelivery.TopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {

    private static final Set<Integer> SUPPORTED_DELAYS = Set.of(5, 30, 300);

    private final OrderProducer orderProducer;
    private final UiStateService uiStateService;
    private final RabbitAdmin rabbitAdmin;

    public ScenarioService(OrderProducer orderProducer, UiStateService uiStateService, RabbitAdmin rabbitAdmin) {
        this.orderProducer = orderProducer;
        this.uiStateService = uiStateService;
        this.rabbitAdmin = rabbitAdmin;
    }

    public List<UiStateService.OrderView> listOrders() {
        return uiStateService.listOrders();
    }

    public void create(String description, int delaySeconds) {
        String normalized = normalize(description);
        if (normalized == null || !SUPPORTED_DELAYS.contains(delaySeconds)) {
            return;
        }
        Order order = uiStateService.createPendingOrder(normalized, delaySeconds);
        orderProducer.sendDelayed(order, delaySeconds);
    }

    public void reset() {
        uiStateService.reset();
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_DELAYED_5S, false);
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_DELAYED_30S, false);
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_DELAYED_5M, false);
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_PROCESS, false);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
