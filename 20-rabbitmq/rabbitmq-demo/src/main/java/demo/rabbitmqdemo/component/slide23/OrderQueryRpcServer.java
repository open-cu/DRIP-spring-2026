package demo.rabbitmqdemo.component.slide23;

import demo.rabbitmqdemo.core.model.OrderRequest;
import demo.rabbitmqdemo.core.model.OrderResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Profile("slide23")
@Component
public class OrderQueryRpcServer {

    @RabbitListener(queues = "order.query.queue")
    public OrderResponse handle(OrderRequest request,
                                @Header(AmqpHeaders.CORRELATION_ID) String correlationId,
                                @Header(AmqpHeaders.REPLY_TO) String replyTo) {
        if (correlationId == null || correlationId.isBlank()) {
            throw new IllegalArgumentException("CorrelationId is required");
        }
        if (!"order.responses".equals(replyTo)) {
            throw new IllegalArgumentException("Unexpected replyTo: " + replyTo);
        }
        return new OrderResponse(request.id(), "FOUND");
    }
}

