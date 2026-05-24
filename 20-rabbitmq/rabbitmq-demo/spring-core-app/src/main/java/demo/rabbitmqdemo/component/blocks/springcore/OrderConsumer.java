package demo.rabbitmqdemo.component.blocks.springcore;

import demo.rabbitmqdemo.config.blocks.springcore.TopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.service.blocks.springcore.UiStateService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final UiStateService uiStateService;

    public OrderConsumer(UiStateService uiStateService) {
        this.uiStateService = uiStateService;
    }

    @RabbitListener(id = "springCoreOrderConsumer", queues = TopologyConfig.QUEUE_ORDERS)
    public void handleOrder(Order order) {
        uiStateService.markProcessed(order);
    }
}
