package demo.rabbitmqdemo.slides.slide21;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import demo.rabbitmqdemo.config.slide21.Slide21OrdersTopologyConfig;
import demo.rabbitmqdemo.slides.slide21.Slide21TestOrdersTopologyConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TDD-кейс №2: OrderProducer отправляет заказ в очередь")
@ActiveProfiles("slide21")
@AutoConfigureMockMvc
@SpringBootTest
@Import({Slide21TestOrdersTopologyConfig.class, Slide21OrdersTopologyConfig.class})
class Slide21ProducerSendsOrderIntegrationTest extends AbstractRabbitMqIT {

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
    }

    @Test
    void producerSendsOrderToRabbitMq() throws Exception {
        Order order = new Order(1L, "Test order");

        mockMvc.perform(post("/api/slide21/producer/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        Order received = awaitMessage(rabbitTemplate, "orders.queue", Order.class);
        assertEquals(order, received);
    }
}
