package demo.rabbitmqdemo.controller.slide34;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.repository.slide34.InMemoryOrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide34")
@RestController
public class Slide34BatchController {

    private final RabbitTemplate rabbitTemplate;
    private final InMemoryOrderRepository repository;

    public Slide34BatchController(RabbitTemplate rabbitTemplate,
                                  InMemoryOrderRepository repository) {
        this.rabbitTemplate = rabbitTemplate;
        this.repository = repository;
    }

    @PostMapping("/api/slide34/batch/publish")
    public void publish(@RequestParam int count) {
        for (int i = 1; i <= count; i++) {
            rabbitTemplate.convertAndSend("batch.queue", new Order((long) i, "Order " + i));
        }
    }

    @GetMapping("/api/slide34/batch/stats")
    public BatchStats stats() {
        return new BatchStats(repository.getSaveCalls(), repository.getTotalItemsSaved(), repository.getLastBatchSize());
    }

    @PostMapping("/api/slide34/batch/reset")
    public void reset() {
        repository.reset();
    }

    public record BatchStats(int saveCalls, int totalItemsSaved, int lastBatchSize) {}
}
