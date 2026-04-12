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

@DisplayName("TDD-кейс №8: Producer может отправлять отложенное сообщение (DLQ+TTL)")
@ActiveProfiles("slide32")
@AutoConfigureMockMvc
@SpringBootTest(properties = "spring.rabbitmq.listener.simple.auto-startup=false")
@Import({Slide32TestDelayedTopologyConfig.class, Slide32DelayedTopologyConfig.class})
class Slide32DelayedMessagesIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        purgeQueues(rabbitAdmin, "delayed.5s.queue", "process.queue");
    }

    @Test
    void messageArrivesAfterDelayViaDlqTtlPattern() throws Exception {
        Order order = new Order(10L, "Delayed order");

        mockMvc.perform(post("/api/slide32/delayed")
                        .queryParam("delaySeconds", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        Awaitility.await()
                .atMost(Duration.ofSeconds(12))
                .untilAsserted(() -> {
                    var delayed5s = rabbitAdmin.getQueueProperties("delayed.5s.queue");
                    assertNotNull(delayed5s);
                    assertNotNull(delayed5s.get(RabbitAdmin.QUEUE_MESSAGE_COUNT));

                    var process = rabbitAdmin.getQueueProperties("process.queue");
                    assertNotNull(process);
                    assertNotNull(process.get(RabbitAdmin.QUEUE_MESSAGE_COUNT));
                });
    }
}
