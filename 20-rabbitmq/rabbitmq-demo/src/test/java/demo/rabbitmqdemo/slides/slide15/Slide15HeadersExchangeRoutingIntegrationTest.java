package demo.rabbitmqdemo.slides.slide15;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import demo.rabbitmqdemo.slides.slide15.Slide15TestRabbitJsonConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("slide15")
@AutoConfigureMockMvc
@SpringBootTest
@Import(Slide15TestRabbitJsonConfig.class)
class Slide15HeadersExchangeRoutingIntegrationTest extends AbstractRabbitMqIT {

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
        purgeQueues(rabbitAdmin, "orders.headers.high.queue");
        purgeQueues(rabbitAdmin, "orders.headers.low.queue");
    }

    @Test
    void routesByHeaderToHighPriorityQueue() throws Exception {
        Order order = new Order(15L, "Header-exchange order");

        mockMvc.perform(post("/api/slide15/orders")
                        .queryParam("priority", "HIGH")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        Object received = rabbitTemplate.receiveAndConvert("orders.headers.high.queue", 5_000);
        assertNotNull(received);
        assertEquals(order, received);

        mockMvc.perform(post("/api/slide15/orders")
                        .queryParam("priority", "LOW")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        received = rabbitTemplate.receiveAndConvert("orders.headers.low.queue", 5_000);
        assertNotNull(received);
        assertEquals(order, received);
    }
}
