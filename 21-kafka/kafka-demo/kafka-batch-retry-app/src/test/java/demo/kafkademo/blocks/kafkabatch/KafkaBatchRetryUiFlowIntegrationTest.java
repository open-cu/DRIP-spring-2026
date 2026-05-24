package demo.kafkademo.blocks.kafkabatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;

import demo.kafkademo.service.BatchUiStateService;
import demo.kafkademo.testsupport.KafkaContainerSupport;
import demo.kafkademo.testsupport.RequiresDocker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@RequiresDocker
@SpringBootTest
@AutoConfigureMockMvc
class KafkaBatchRetryUiFlowIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BatchUiStateService stateService;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        KafkaContainerSupport.register(registry);
    }

    @BeforeEach
    void resetState() {
        stateService.reset();
    }

    @Test
    void rootPathAndBlockPathOpenBatchPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-batch-retry: читаем пачкой")));

        mockMvc.perform(get("/blocks/kafka-batch-retry"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-batch-retry: читаем пачкой")));
    }

    @Test
    void batchListenerProcessesPublishedEvents() throws Exception {
        mockMvc.perform(post("/blocks/kafka-batch-retry/publish-batch").param("count", "3"))
                .andExpect(status().is3xxRedirection());

        await().atMost(Duration.ofSeconds(15)).untilAsserted(() ->
                assertThat(stateService.pageData().processed()).isEqualTo(3)
        );

        mockMvc.perform(get("/blocks/kafka-batch-retry"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ОБРАБОТАН")));
    }

    @Test
    void failureInsideBatchIsVisibleAndGoesToDlt() throws Exception {
        mockMvc.perform(post("/blocks/kafka-batch-retry/publish-failure"))
                .andExpect(status().is3xxRedirection());

        await().atMost(Duration.ofSeconds(15)).untilAsserted(() -> {
            BatchUiStateService.PageData data = stateService.pageData();
            assertThat(data.failed()).isGreaterThanOrEqualTo(1);
            assertThat(data.sentToDlt()).isGreaterThanOrEqualTo(1);
        });

        mockMvc.perform(get("/blocks/kafka-batch-retry"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("DLT")))
                .andExpect(content().string(containsString("partial failure")));
    }
}
