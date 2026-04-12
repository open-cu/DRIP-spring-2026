package demo.rabbitmqdemo.slides.slide32;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import demo.rabbitmqdemo.config.slide32.Slide32DelayedTopologyConfig;
import demo.rabbitmqdemo.slides.slide32.Slide32TestDelayedTopologyConfig;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TDD-кейс №10.5: DelayedProducer выбирает правильную очередь")
@ActiveProfiles("slide32")
@AutoConfigureMockMvc
@SpringBootTest
@Import({Slide32TestDelayedTopologyConfig.class, Slide32DelayedTopologyConfig.class})
class Slide32DelayedQueueSelectionIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        purgeQueues(rabbitAdmin, "delayed.30s.queue");
    }

    @Test
    void selects30sQueue() throws Exception {
        Order order = new Order(360L, "Delayed 30s");

        mockMvc.perform(post("/api/slide32/delayed")
                        .queryParam("delaySeconds", "30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        Awaitility.await()
                .atMost(Duration.ofSeconds(3))
                .untilAsserted(() -> {
                    var props = rabbitAdmin.getQueueProperties("delayed.30s.queue");
                    assertNotNull(props);
                    Object count = props.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);
                    assertNotNull(count);
                    if (((Number) count).intValue() < 1) {
                        throw new AssertionError("No message in delayed.30s.queue yet");
                    }
                });
    }

    @Test
    void unsupportedDelayReturnsServerError() throws Exception {
        Order order = new Order(361L, "Unsupported delay");

        Assertions.assertThrows(Exception.class, () ->
                mockMvc.perform(post("/api/slide32/delayed")
                        .queryParam("delaySeconds", "60")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))));
    }
}
