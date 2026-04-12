package demo.rabbitmqdemo.controller.slide22;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.service.slide22.OrderProducerWithHeaders;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide22")
@RestController
public class Slide22HeadersController {

    private final OrderProducerWithHeaders producer;

    public Slide22HeadersController(OrderProducerWithHeaders producer) {
        this.producer = producer;
    }

    @PostMapping("/api/slide22/orders")
    public void send(@RequestBody Order order,
                     @RequestParam String priority) {
        producer.sendOrderWithHeaders(order, priority);
    }
}

