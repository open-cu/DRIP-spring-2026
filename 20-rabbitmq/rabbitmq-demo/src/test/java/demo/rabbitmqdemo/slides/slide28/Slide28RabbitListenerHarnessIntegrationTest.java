package demo.rabbitmqdemo.slides.slide28;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import demo.rabbitmqdemo.slides.slide28.Slide28TestRabbitJsonConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("TDD-кейс №9: Integration test с @RabbitListenerTest (Harness)")
@ActiveProfiles("slide28")
@RabbitListenerTest(capture = true)
@SpringBootTest
@Import({Slide28TestRabbitJsonConfig.class, Slide28RabbitListenerHarnessIntegrationTest.TestRabbitListenerConfig.class})
class Slide28RabbitListenerHarnessIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitListenerTestHarness harness;

    @BeforeEach
    void setupQueue() {
        purgeQueues(rabbitAdmin, "test.orders.queue");
    }

    @Test
    void shouldReceiveOrder() throws Exception {
        Order testOrder = new Order(1L, "Test Order");

        rabbitTemplate.convertAndSend("test.orders.queue", testOrder);

        RabbitListenerTestHarness.InvocationData data = awaitInvocation(harness, TestRabbitListenerConfig.LISTENER_ID, 5);
        assertNotNull(data.getArguments());
        assertEquals(1, data.getArguments().length);
        assertEquals(testOrder, data.getArguments()[0]);
    }

    @TestConfiguration
    static class TestRabbitListenerConfig {

        static final String LISTENER_ID = "slide28TestOrdersListener";

        @Bean
        Queue testOrdersQueue() {
            return QueueBuilder.durable("test.orders.queue").build();
        }

        @RabbitListener(id = LISTENER_ID, queues = "test.orders.queue")
        void handle(Order order) {
            // captured by RabbitListenerTestHarness
        }
    }
}
