package demo.rabbitmqdemo.slides.slide13;

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

@DisplayName("Slide 13: topic exchange routes by pattern")
@ActiveProfiles("slide13")
@SpringBootTest
class Slide13TopicExchangeRoutingIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        rabbitAdmin.initialize();
        purgeQueues(rabbitAdmin, "orders.queue");
        purgeQueues(rabbitAdmin, "notifications.queue");
    }

    @Test
    void routesMessageWhenRoutingKeyMatchesPattern() {
        rabbitTemplate.convertAndSend("common.topic", "order.created", "topic-message");

        Object received = rabbitTemplate.receiveAndConvert("orders.queue", 5_000);
        assertNotNull(received);
        assertEquals("topic-message", received);

        rabbitTemplate.convertAndSend("common.topic", "notifications.topic.test", "topic-message-2");

        received = rabbitTemplate.receiveAndConvert("notifications.queue", 5_000);
        assertNotNull(received);
        assertEquals("topic-message-2", received);
    }
}
