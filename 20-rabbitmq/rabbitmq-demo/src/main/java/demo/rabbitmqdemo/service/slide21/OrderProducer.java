package demo.rabbitmqdemo.service.slide21;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;

@Profile("slide21")
@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend("orders.direct", "order.created", order);
    }
}
