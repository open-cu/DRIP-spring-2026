package demo.rabbitmqdemo.service.slide23;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import java.util.UUID;
import demo.rabbitmqdemo.core.model.OrderRequest;
import demo.rabbitmqdemo.core.model.OrderResponse;
import org.springframework.context.annotation.Profile;

@Profile("slide23")
@Service
public class OrderQueryService {

    private final RabbitTemplate rabbitTemplate;

    public OrderQueryService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public OrderResponse sendAndWait(OrderRequest request) {
        String correlationId = UUID.randomUUID().toString();

        rabbitTemplate.convertAndSend(
                "orders.direct",
                "order.query",
                request,
                message -> prepareRpcRequest(message, correlationId)
        );

        Message reply = rabbitTemplate.receive("order.responses", 5_000);
        if (reply == null) {
            throw new IllegalStateException("RPC reply timeout");
        }
        if (!correlationId.equals(reply.getMessageProperties().getCorrelationId())) {
            throw new IllegalStateException("Unexpected correlationId in reply");
        }
        MessageConverter converter = rabbitTemplate.getMessageConverter();
        Object response = converter.fromMessage(reply);
        return (OrderResponse) response;
    }

    private Message prepareRpcRequest(Message message, String correlationId) {
        message.getMessageProperties().setCorrelationId(correlationId);
        message.getMessageProperties().setReplyTo("order.responses");
        return message;
    }
}
