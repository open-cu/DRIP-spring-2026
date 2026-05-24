package demo.rabbitmqdemo.config.blocks.springcore;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TopologyConfig {

    public static final String EXCHANGE_ORDERS = "springcore.orders.direct";
    public static final String QUEUE_ORDERS = "springcore.orders.queue";
    public static final String ROUTING_KEY_ORDER_CREATED = "springcore.order.created";
    public static final String QUEUE_TEXT_ORDERS = "springcore.orders.text.queue";
    public static final String ROUTING_KEY_TEXT_CREATED = "springcore.order.text.created";
    public static final String QUEUE_RPC = "springcore.order.query.queue";
    public static final String ROUTING_KEY_ORDER_QUERY = "springcore.order.query";

    @Bean
    public Declarables topology() {
        DirectExchange exchange = new DirectExchange(EXCHANGE_ORDERS);

        Queue ordersQueue = QueueBuilder.durable(QUEUE_ORDERS).build();
        Binding orderCreatedBinding = BindingBuilder.bind(ordersQueue).to(exchange).with(ROUTING_KEY_ORDER_CREATED);

        Queue textOrdersQueue = QueueBuilder.durable(QUEUE_TEXT_ORDERS).build();
        Binding textOrderBinding = BindingBuilder.bind(textOrdersQueue).to(exchange).with(ROUTING_KEY_TEXT_CREATED);

        Queue rpcQueue = QueueBuilder.durable(QUEUE_RPC).build();
        Binding orderQueryBinding = BindingBuilder.bind(rpcQueue).to(exchange).with(ROUTING_KEY_ORDER_QUERY);

        return new Declarables(
                exchange,
                ordersQueue,
                orderCreatedBinding,
                textOrdersQueue,
                textOrderBinding,
                rpcQueue,
                orderQueryBinding
        );
    }
}
