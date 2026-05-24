package demo.rabbitmqdemo.component.blocks.springcore;

import demo.rabbitmqdemo.config.blocks.springcore.TopologyConfig;
import demo.rabbitmqdemo.service.blocks.springcore.UiStateService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TextConsumer {

    private final UiStateService uiStateService;

    public TextConsumer(UiStateService uiStateService) {
        this.uiStateService = uiStateService;
    }

    @RabbitListener(id = "springCoreTextConsumer", queues = TopologyConfig.QUEUE_TEXT_ORDERS)
    public void handleText(String message) {
        uiStateService.markSimpleTextProcessed(message);
    }
}
