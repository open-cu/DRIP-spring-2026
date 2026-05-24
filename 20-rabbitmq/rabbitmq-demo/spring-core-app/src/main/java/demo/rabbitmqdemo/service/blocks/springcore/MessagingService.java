package demo.rabbitmqdemo.service.blocks.springcore;

import demo.rabbitmqdemo.config.blocks.springcore.TopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.core.model.OrderRequest;
import demo.rabbitmqdemo.core.model.OrderResponse;
import demo.rabbitmqdemo.service.blocks.springcore.ScenarioService.SendTextMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {
    private final RabbitTemplate rabbitTemplate;
    private final UiStateService uiStateService;

    public MessagingService(RabbitTemplate rabbitTemplate, UiStateService uiStateService) {
        this.rabbitTemplate = rabbitTemplate;
        this.uiStateService = uiStateService;
    }

    public void createAndSendOrder(String description) {
        String normalized = normalize(description);
        if (normalized == null) {
            return;
        }
        Order order = uiStateService.createOrder(normalized);
        rabbitTemplate.convertAndSend(
                TopologyConfig.EXCHANGE_ORDERS,
                TopologyConfig.ROUTING_KEY_ORDER_CREATED,
                order
        );
    }

    public void requestAndStoreStatus(Long orderId) {
        if (orderId == null || orderId <= 0) {
            return;
        }
        OrderResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                TopologyConfig.EXCHANGE_ORDERS,
                TopologyConfig.ROUTING_KEY_ORDER_QUERY,
                new OrderRequest(orderId),
                new ParameterizedTypeReference<>() {
                }
        );
        if (response != null) {
            uiStateService.markRpcResult(response);
        }
    }

    public void sendSimpleText(String text, SendTextMode mode) {
        String normalized = normalize(text);
        if (normalized == null) {
            return;
        }
        uiStateService.registerSimpleText(normalized);

        if (mode == SendTextMode.EXPLICIT) {
            rabbitTemplate.convertAndSend(
                    TopologyConfig.EXCHANGE_ORDERS,
                    TopologyConfig.ROUTING_KEY_TEXT_CREATED,
                    normalized
            );
            return;
        }
        rabbitTemplate.convertAndSend(normalized);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
