package demo.rabbitmqdemo.slides.slide14;

import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Slide 14: fanout exchange broadcasts to bound queue")
@ActiveProfiles("slide14")
@SpringBootTest
class Slide14FanoutExchangeRoutingIntegrationTest extends AbstractRabbitMqIT {
    private final String notificationMoscowQueueName = "notifications.moscow.queue";
    private final String notificationPscovQueueName = "notifications.pscov.queue";
    private final String notificationTverQueueName = "notifications.tver.queue";
    private final String notificationElistaQueue = "notifications.elista.queue";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        purgeQueues(rabbitAdmin, notificationMoscowQueueName);
        purgeQueues(rabbitAdmin, notificationPscovQueueName);
        purgeQueues(rabbitAdmin, notificationTverQueueName);
        purgeQueues(rabbitAdmin, notificationElistaQueue);
    }

    @Test
    void routesMessageWithoutRoutingKeyMatch() {
        rabbitTemplate.convertAndSend("notifications.fanout", "", "fanout-message");

        Object received = rabbitTemplate.receiveAndConvert(notificationMoscowQueueName, 5_000);
        assertNotNull(received);
        assertEquals("fanout-message", received);
        received = rabbitTemplate.receiveAndConvert(notificationPscovQueueName, 5_000);
        assertNotNull(received);
        assertEquals("fanout-message", received);
        received = rabbitTemplate.receiveAndConvert(notificationTverQueueName, 5_000);
        assertNotNull(received);
        assertEquals("fanout-message", received);
        received = rabbitTemplate.receiveAndConvert(notificationElistaQueue, 5_000);
        assertNotNull(received);
        assertEquals("fanout-message", received);
    }
}
