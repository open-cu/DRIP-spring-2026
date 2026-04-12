package demo.rabbitmqdemo.slides.slide22;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.rabbitmqdemo.config.slide22.RabbitJsonConfig;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import demo.rabbitmqdemo.config.slide22.Slide22OrdersTopologyConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("TDD-кейс №3: JSON конвертер корректно конвертирует Order")
@ActiveProfiles("slide22")
@SpringBootTest
@Import({RabbitJsonConfig.class, Slide22OrdersTopologyConfig.class})
class Slide22JsonConverterIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        rabbitAdmin.initialize();
        purgeQueues(rabbitAdmin, "orders.queue");
    }

    @Test
    void convertsOrderToJsonMessage() throws Exception {
        Order order = new Order(123L, "Test Order");

        rabbitTemplate.convertAndSend("orders.direct", "order.created", order);

        Message message = rabbitTemplate.receive("orders.queue", 5_000);
        assertNotNull(message);
        assertNotNull(message.getMessageProperties().getContentType());

        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        JsonNode json = objectMapper.readTree(body);
        assertEquals(123L, json.get("id").asLong());
        assertEquals("Test Order", json.get("description").asText());
    }
}
