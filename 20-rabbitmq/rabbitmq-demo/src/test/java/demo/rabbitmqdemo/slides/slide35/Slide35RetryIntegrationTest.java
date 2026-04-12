package demo.rabbitmqdemo.slides.slide35;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TDD-кейс №13: Retry при ошибке (maxAttempts=3)")
@ActiveProfiles("slide35")
@AutoConfigureMockMvc
@SpringBootTest
class Slide35RetryIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void clean() {
        purgeQueues(rabbitAdmin, "orders.retry.queue", "orders.retry.dlq");
    }

    @Test
    void consumerRetriesThreeTimesOnFailure() throws Exception {
        Order order = new Order(999L, "Fail order");

        mockMvc.perform(post("/api/slide35/retry/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        assertEquals(3, awaitAttempts());
    }

    @Test
    void messageGoesToDlqAfterRetriesExhausted() throws Exception {
        Order order = new Order(999L, "Fail order");

        mockMvc.perform(post("/api/slide35/retry/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        assertEquals(3, awaitAttempts());
        Order dlqMessage = awaitMessage(rabbitTemplate, "orders.retry.dlq", Order.class);
        assertEquals(order, dlqMessage);
    }

    private int awaitAttempts() {
        return org.awaitility.Awaitility.await()
                .atMost(java.time.Duration.ofSeconds(15))
                .until(this::readAttempts, attempts -> attempts == 3);
    }

    private int readAttempts() {
        try {
            return Integer.parseInt(mockMvc.perform(get("/api/slide35/retry/attempts"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString()
                    .trim());
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}
