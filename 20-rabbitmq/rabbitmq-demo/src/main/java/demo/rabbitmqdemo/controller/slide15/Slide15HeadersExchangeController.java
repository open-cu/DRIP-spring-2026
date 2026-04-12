package demo.rabbitmqdemo.controller.slide15;

import demo.rabbitmqdemo.core.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide15")
@RestController
public class Slide15HeadersExchangeController {

    private final RabbitTemplate rabbitTemplate;

    public Slide15HeadersExchangeController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/slide15/orders")
    public void send(@RequestBody Order order,
                     @RequestParam String priority) {
        rabbitTemplate.convertAndSend(
                "orders.headers",
                "",
                order,
                message -> {
                    message.getMessageProperties().setHeader("X-Priority", priority);
                    return message;
                }
        );
    }
}
