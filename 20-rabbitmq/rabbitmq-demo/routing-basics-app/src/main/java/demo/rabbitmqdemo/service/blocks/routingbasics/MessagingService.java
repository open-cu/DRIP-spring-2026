package demo.rabbitmqdemo.service.blocks.routingbasics;

import java.util.concurrent.atomic.AtomicLong;

import demo.rabbitmqdemo.core.model.Order;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {
    private static final String PRIORITY_HEADER = "X-Priority";

    private final RabbitTemplate rabbitTemplate;
    private final RoutingTopologyView topology;
    private final AtomicLong ids = new AtomicLong(3000);

    public MessagingService(RabbitTemplate rabbitTemplate, RoutingTopologyView topology) {
        this.rabbitTemplate = rabbitTemplate;
        this.topology = topology;
    }

    public void sendDirect(String routingKey, String description) {
        String normalizedRoutingKey = normalize(routingKey);
        String normalizedDescription = normalize(description);
        if (normalizedRoutingKey == null || normalizedDescription == null) {
            return;
        }
        rabbitTemplate.convertAndSend(
                topology.directExchange(),
                normalizedRoutingKey,
                new Order(ids.incrementAndGet(), normalizedDescription)
        );
    }

    public void sendTopic(String routingKey, String description) {
        String normalizedRoutingKey = normalize(routingKey);
        String normalizedDescription = normalize(description);
        if (normalizedRoutingKey == null || normalizedDescription == null) {
            return;
        }
        rabbitTemplate.convertAndSend(
                topology.topicExchange(),
                normalizedRoutingKey,
                new Order(ids.incrementAndGet(), normalizedDescription)
        );
    }

    public void sendFanout(String description) {
        String normalizedDescription = normalize(description);
        if (normalizedDescription == null) {
            return;
        }
        rabbitTemplate.convertAndSend(
                topology.fanoutExchange(),
                "",
                new Order(ids.incrementAndGet(), normalizedDescription)
        );
    }

    public void sendHeaders(String priority, String description) {
        String normalizedPriority = normalize(priority);
        String normalizedDescription = normalize(description);
        if (normalizedPriority == null || normalizedDescription == null) {
            return;
        }
        rabbitTemplate.convertAndSend(
                topology.headersExchange(),
                "",
                new Order(ids.incrementAndGet(), normalizedDescription),
                priorityPostProcessor(normalizedPriority)
        );
    }

    private org.springframework.amqp.core.MessagePostProcessor priorityPostProcessor(String priority) {
        return message -> {
            message.getMessageProperties().setHeader(PRIORITY_HEADER, priority);
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        };
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
