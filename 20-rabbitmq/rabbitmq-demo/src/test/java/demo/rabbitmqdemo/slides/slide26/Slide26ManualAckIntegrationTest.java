package demo.rabbitmqdemo.slides.slide26;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.config.slide26.Slide26OrdersTopologyConfig;
import demo.rabbitmqdemo.config.slide26.Slide26RabbitJsonConfig;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DisplayName("TDD-кейс №7: Ручной acknowledgment (ack/nack без requeue)")
@ActiveProfiles("slide26")
@RabbitListenerTest(capture = true)
@SpringBootTest
@Import({Slide26RabbitJsonConfig.class, Slide26OrdersTopologyConfig.class})
class Slide26ManualAckIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        purgeQueues(rabbitAdmin, "orders.queue");
    }

    @Test
    void onFailureMessageIsNackedAndNotRequeued() throws Exception {
        Order order = new Order(999L, "Fail order");

        rabbitTemplate.convertAndSend("orders.direct", "order.created", order);

        Awaitility.await()
                .atMost(Duration.ofSeconds(8))
                .untilAsserted(() -> assertEquals(0, rabbitAdmin.getQueueInfo("orders.queue").getMessageCount()));
    }
}
