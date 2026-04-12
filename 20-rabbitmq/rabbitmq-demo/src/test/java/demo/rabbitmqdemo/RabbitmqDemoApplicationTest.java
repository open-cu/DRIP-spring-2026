package demo.rabbitmqdemo;

import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("slide22")
@SpringBootTest
class RabbitmqDemoApplicationTest extends AbstractRabbitMqIT {
    @Test
    void contextLoads() {
    }
}
