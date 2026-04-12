package demo.rabbitmqdemo.controller.slide21;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.service.slide21.OrderProducer;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide21")
@RestController
public class Slide21ProducerController {

    private final OrderProducer producer;

    public Slide21ProducerController(OrderProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/api/slide21/producer/orders")
    public void send(@RequestBody Order order) {
        producer.sendOrder(order);
    }
}

