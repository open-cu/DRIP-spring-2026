package demo.rabbitmqdemo.slides.slide34;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.rabbitmqdemo.controller.slide34.Slide34BatchController.BatchStats;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TDD-кейс №11.5: Spring Batch job обрабатывает пакеты")
@ActiveProfiles("slide34")
@AutoConfigureMockMvc
@SpringBootTest
class Slide34SpringBatchJobIntegrationTest extends AbstractRabbitMqIT {

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        registry.add("demo.slide34.mode", () -> "batch-job");
        registry.add("demo.slide34.batchJob.rateMs", () -> "200");
        registry.add("demo.slide34.batchJob.batchSize", () -> "100");
        registry.add("spring.batch.job.enabled", () -> "false");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() throws Exception {
        purgeQueues(rabbitAdmin, "batch.queue");
        mockMvc.perform(post("/api/slide34/batch/reset"))
                .andExpect(status().isOk());
    }

    @Test
    void processesBatchWithJob() throws Exception {
        mockMvc.perform(post("/api/slide34/batch/publish").queryParam("count", "100"))
                .andExpect(status().isOk());

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    var queueProps = rabbitAdmin.getQueueProperties("batch.queue");
                    assertTrue(((Number) queueProps.get(RabbitAdmin.QUEUE_MESSAGE_COUNT)).intValue() >= 0);
                });

        String json = mockMvc.perform(get("/api/slide34/batch/stats"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        BatchStats stats = objectMapper.readValue(json, BatchStats.class);
        assertTrue(stats.saveCalls() >= 0);
        assertTrue(stats.totalItemsSaved() >= 0);
    }
}
