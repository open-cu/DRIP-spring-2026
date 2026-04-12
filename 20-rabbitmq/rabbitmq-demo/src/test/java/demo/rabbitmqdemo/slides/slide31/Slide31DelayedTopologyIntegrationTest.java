package demo.rabbitmqdemo.slides.slide31;

import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("TDD-кейс №9: DLQ topology создаётся корректно (очереди/обменник существуют)")
@ActiveProfiles("slide31")
@SpringBootTest
class Slide31DelayedTopologyIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    void topologyExists() {
        assertNotNull(rabbitAdmin.getQueueProperties("delayed.queue"));
        assertNotNull(rabbitAdmin.getQueueProperties("process.queue"));
        assertNotNull(rabbitAdmin.getQueueProperties("delayed.5s.queue"));
        assertNotNull(rabbitAdmin.getQueueProperties("delayed.30s.queue"));
        assertNotNull(rabbitAdmin.getQueueProperties("delayed.5m.queue"));
    }
}
