package demo.rabbitmqdemo.service.slide32;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;

@Profile("slide32")
@Service
public class DelayedProducer {

    private final RabbitTemplate rabbitTemplate;
    private final Map<Integer, String> delayQueues = Map.of(
        5, "delayed.5s.queue",
        30, "delayed.30s.queue",
        300, "delayed.5m.queue"
    );

    public DelayedProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDelayed(Order order, int delaySeconds) {
        String queue = delayQueues.get(delaySeconds);
        if (queue == null) {
            throw new IllegalArgumentException("Unsupported delay: " + delaySeconds);
        }
        rabbitTemplate.convertAndSend(queue, order);
    }
}
