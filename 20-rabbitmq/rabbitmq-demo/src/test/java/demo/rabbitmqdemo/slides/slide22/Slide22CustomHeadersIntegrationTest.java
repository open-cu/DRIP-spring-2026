package demo.rabbitmqdemo.slides.slide22;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.rabbitmqdemo.config.slide22.RabbitJsonConfig;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TDD-кейс №3.5: Отправка с custom headers")
@ActiveProfiles("slide22")
@AutoConfigureMockMvc
@SpringBootTest
@Import(RabbitJsonConfig.class)
class Slide22CustomHeadersIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        rabbitAdmin.initialize();
        purgeQueues(rabbitAdmin, "orders.queue");
        rabbitAdmin.declareExchange(new DirectExchange("orders.direct", true, false));
        rabbitAdmin.declareQueue(new Queue("orders.queue", true));
        rabbitAdmin.declareBinding(new Binding(
                "orders.queue",
                DestinationType.QUEUE,
                "orders.direct",
                "order.created",
                null
        ));
    }

    @Test
    void producerAddsCustomHeadersAndDeliveryMode() throws Exception {
        Order order = new Order(2L, "Header order");

        mockMvc.perform(post("/api/slide22/orders")
                        .queryParam("priority", "HIGH")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        Message message = rabbitTemplate.receive("orders.queue", 5_000);
        assertNotNull(message);
        assertEquals("HIGH", message.getMessageProperties().getHeaders().get("X-Priority"));
    }
}
