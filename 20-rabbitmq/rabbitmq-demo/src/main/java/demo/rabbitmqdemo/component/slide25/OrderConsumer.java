package demo.rabbitmqdemo.component.slide25;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;

@Profile("slide25")
@Slf4j
@Component
public class OrderConsumer {

    @RabbitListener(
        id = "slide25OrderConsumer",
        bindings = @QueueBinding(
            value = @Queue(value = "orders.queue", durable = "true"),
            exchange = @Exchange(value = "orders.direct", type = ExchangeTypes.DIRECT),
            key = "order.created"
        )
    )
    public void handleOrder(Order order) {
        log.info("Received order: {}", order);
    }
}
