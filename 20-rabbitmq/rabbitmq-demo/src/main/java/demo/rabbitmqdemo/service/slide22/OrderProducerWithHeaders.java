package demo.rabbitmqdemo.service.slide22;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;

@Profile("slide22")
@Service
public class OrderProducerWithHeaders {

    private final RabbitTemplate rabbitTemplate;

    public OrderProducerWithHeaders(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderWithHeaders(Order order, String priority) {
        rabbitTemplate.convertAndSend(
            "orders.direct",
            "order.created",
            order,
            new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) {
                    message.getMessageProperties().setHeader("X-Priority", priority);
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                }
            }
        );
    }
}
