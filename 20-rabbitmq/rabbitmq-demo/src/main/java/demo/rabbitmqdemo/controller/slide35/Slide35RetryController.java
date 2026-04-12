package demo.rabbitmqdemo.controller.slide35;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.service.slide35.RetryAttemptsProbe;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide35")
@RestController
public class Slide35RetryController {

    private final RabbitTemplate rabbitTemplate;
    private final RetryAttemptsProbe probe;

    public Slide35RetryController(RabbitTemplate rabbitTemplate,
                                  RetryAttemptsProbe probe) {
        this.rabbitTemplate = rabbitTemplate;
        this.probe = probe;
    }

    @PostMapping("/api/slide35/retry/orders")
    public void publish(@RequestBody Order order) {
        probe.reset();
        rabbitTemplate.convertAndSend("orders.retry.queue", order);
    }

    @GetMapping("/api/slide35/retry/attempts")
    public int attempts() {
        return probe.getAttempts();
    }
}
