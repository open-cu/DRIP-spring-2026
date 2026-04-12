package demo.rabbitmqdemo.controller.slide21;

import demo.rabbitmqdemo.core.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide21")
@RestController
public class Slide21TemplateDefaultsController {

    private final RabbitTemplate rabbitTemplate;

    public Slide21TemplateDefaultsController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/slide21/orders")
    public void send(@RequestBody Order order) {
        rabbitTemplate.convertAndSend(order);
    }
}

