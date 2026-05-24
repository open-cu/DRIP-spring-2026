package demo.rabbitmqdemo.service.blocks.delayeddelivery;

import java.util.Map;

import demo.rabbitmqdemo.config.blocks.delayeddelivery.TopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;
    private final Map<Integer, String> delayRoutingKeys = Map.of(
            5, TopologyConfig.ROUTING_KEY_DELAY_5S,
            30, TopologyConfig.ROUTING_KEY_DELAY_30S,
            300, TopologyConfig.ROUTING_KEY_DELAY_5M
    );

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDelayed(Order order, int delaySeconds) {
        if (order == null || order.id() == null) {
            return;
        }
        String routingKey = delayRoutingKeys.get(delaySeconds);
        if (routingKey == null) {
            throw new IllegalArgumentException("Неподдерживаемая задержка: " + delaySeconds);
        }
        rabbitTemplate.convertAndSend(TopologyConfig.EXCHANGE_WAIT, routingKey, order);
    }
}
