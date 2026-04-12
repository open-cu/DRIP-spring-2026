package demo.rabbitmqdemo.component.slide27;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;

@Profile("slide27")
@Component
public class OrderConsumer {

    @RabbitListener(
        id = "slide27OrderConsumer",
        queues = "orders.queue",
        concurrency = "3-10"
    )
    public void handleOrder(Order order) {
        // обработка заказа
    }
}
