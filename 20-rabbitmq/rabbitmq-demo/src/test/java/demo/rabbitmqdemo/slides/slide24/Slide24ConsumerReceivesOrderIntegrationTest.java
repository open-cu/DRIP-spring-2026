package demo.rabbitmqdemo.slides.slide24;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.config.slide24.Slide24OrdersTopologyConfig;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import demo.rabbitmqdemo.slides.slide24.Slide24TestOrdersTopologyConfig;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TDD-кейс №5: Consumer получает заказ из очереди")
@ActiveProfiles("slide24")
@RabbitListenerTest(capture = true)
@SpringBootTest
@Import({Slide24TestOrdersTopologyConfig.class, Slide24OrdersTopologyConfig.class})
class Slide24ConsumerReceivesOrderIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitListenerTestHarness harness;

    @BeforeEach
    void clean() {
        rabbitAdmin.initialize();
        purgeQueues(rabbitAdmin, "orders.queue");
    }

    @Test
    void consumerInvokedForOrder() throws Exception {
        Order order = new Order(29L, "Order for consumer");

        rabbitTemplate.convertAndSend("orders.direct", "order.created", order);

        RabbitListenerTestHarness.InvocationData data = awaitInvocation(harness, "slide24OrderConsumer", 5);
        assertEquals(order, data.getArguments()[0]);
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> assertEquals(0, rabbitAdmin.getQueueInfo("orders.queue").getMessageCount()));
    }
}
