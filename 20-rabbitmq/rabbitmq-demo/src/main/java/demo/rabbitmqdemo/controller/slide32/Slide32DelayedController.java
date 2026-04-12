package demo.rabbitmqdemo.controller.slide32;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.service.slide32.DelayedProducer;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide32")
@RestController
public class Slide32DelayedController {

    private final DelayedProducer producer;

    public Slide32DelayedController(DelayedProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/api/slide32/delayed")
    public void sendDelayed(@RequestBody Order order,
                            @RequestParam int delaySeconds) {
        producer.sendDelayed(order, delaySeconds);
    }
}

