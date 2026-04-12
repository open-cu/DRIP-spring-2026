package demo.rabbitmqdemo.component.slide26;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;

@Profile("slide26")
@Slf4j
@Component
public class ManualAckOrderConsumer {

    @RabbitListener(id = "slide26ManualAckOrderConsumer", queues = "orders.queue")
    public void handleOrder(Order order,
                         Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            processOrder(order);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Error processing order", e);
            try {
                channel.basicNack(deliveryTag, false, false); // без requeue
            } catch (java.io.IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    private void processOrder(Order order) {
        // бизнес-логика
        if (order != null && Long.valueOf(999L).equals(order.getId())) {
            throw new IllegalStateException("Simulated processing failure");
        }
    }
}
