package demo.rabbitmqdemo.component.blocks.springcore;

import demo.rabbitmqdemo.config.blocks.springcore.TopologyConfig;
import demo.rabbitmqdemo.core.model.OrderRequest;
import demo.rabbitmqdemo.core.model.OrderResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RpcServer {

    @RabbitListener(id = "springCoreRpcServer", queues = TopologyConfig.QUEUE_RPC)
    public OrderResponse handle(OrderRequest request) {
        return new OrderResponse(request.id(), "НАЙДЕН");
    }
}
