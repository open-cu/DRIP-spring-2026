package demo.rabbitmqdemo.slides.slide12;

import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Slide 12: direct exchange routes by exact routing key")
@ActiveProfiles("slide12")
@SpringBootTest
class Slide12DirectExchangeRoutingIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        rabbitAdmin.initialize();
        purgeQueues(rabbitAdmin, "orders.queue");
        purgeQueues(rabbitAdmin, "billing.queue");
    }

    @Test
    void routesMessageToBoundQueue() {
        rabbitTemplate.convertAndSend("orders.direct", "order.created", "direct-message");

        Object received = rabbitTemplate.receiveAndConvert("orders.queue", 5_000);
        assertNotNull(received);
        assertEquals("direct-message", received);

        rabbitTemplate.convertAndSend("orders.direct", "order.paid", "direct-message-2");

        received = rabbitTemplate.receiveAndConvert("billing.queue", 5_000);
        assertNotNull(received);
        assertEquals("direct-message-2", received);
    }
}
