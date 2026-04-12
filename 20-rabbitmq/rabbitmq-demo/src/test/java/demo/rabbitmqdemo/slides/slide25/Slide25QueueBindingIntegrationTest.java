package demo.rabbitmqdemo.slides.slide25;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.config.slide25.Slide25RabbitJsonConfig;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TDD-кейс №6: Декларативная конфигурация через @QueueBinding")
@ActiveProfiles("slide25")
@RabbitListenerTest(capture = true)
@SpringBootTest
@Import(Slide25RabbitJsonConfig.class)
class Slide25QueueBindingIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    void sendingToExchangeRoutesAndInvokesListener() throws Exception {
        Order order = new Order(31L, "Order via QueueBinding");

        rabbitTemplate.convertAndSend("orders.direct", "order.created", order);

        RabbitListenerTestHarness.InvocationData data = awaitInvocation(harness, "slide25OrderConsumer", 5);
        assertEquals(order, data.getArguments()[0]);
    }
}
