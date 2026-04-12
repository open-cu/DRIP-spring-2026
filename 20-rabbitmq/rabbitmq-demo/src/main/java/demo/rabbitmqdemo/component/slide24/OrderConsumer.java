package demo.rabbitmqdemo.component.slide24;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;

@Profile("slide24")
@Slf4j
@Component
public class OrderConsumer {

    @RabbitListener(id = "slide24OrderConsumer", queues = "orders.queue")
    public void handleOrder(Order order) {
        log.info("Received order: {}", order);
        // обработка заказа
    }
}
