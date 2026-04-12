package demo.rabbitmqdemo.component.slide32;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;

@Profile("slide32")
@Slf4j
@Component
public class DelayedOrderConsumer {

    @RabbitListener(id = "slide32DelayedOrderConsumer", queues = "process.queue")
    public void handleDelayedOrder(Order order) {
        log.info("Processing delayed order: {}", order);
        // Заказ обрабатывается только через указанное время
    }
}
