package demo.rabbitmqdemo.component.blocks.delayeddelivery;

import demo.rabbitmqdemo.config.blocks.delayeddelivery.TopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.service.blocks.delayeddelivery.UiStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {

    private final UiStateService uiStateService;

    public OrderConsumer(UiStateService uiStateService) {
        this.uiStateService = uiStateService;
    }

    @RabbitListener(queues = TopologyConfig.QUEUE_PROCESS)
    public void handleDelayedOrder(Order order) {
        log.info("Обработка отложенного заказа: {}", order);
        uiStateService.markProcessed(order);
    }
}
