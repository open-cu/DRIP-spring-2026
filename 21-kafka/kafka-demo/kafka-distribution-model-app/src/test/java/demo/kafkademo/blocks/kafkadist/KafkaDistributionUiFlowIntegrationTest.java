package demo.kafkademo.blocks.kafkadist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.List;

import demo.kafkademo.service.DistributionUiStateService;
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
class KafkaDistributionUiFlowIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    DistributionUiStateService stateService;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        KafkaContainerSupport.register(registry);
    }

    @BeforeEach
    void resetState() {
        stateService.reset();
    }

    @Test
    void rootPathAndBlockPathOpenDistributionPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-distribution-model: key, partition, consumer group")));

        mockMvc.perform(get("/blocks/kafka-distribution-model"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-distribution-model: key, partition, consumer group")));
    }

    @Test
    void sameKeyIsShownOnSamePartition() throws Exception {
        mockMvc.perform(post("/blocks/kafka-distribution-model/events")
                        .param("eventKey", "order-777")
                        .param("payload", "first"))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(post("/blocks/kafka-distribution-model/events")
                        .param("eventKey", "order-777")
                        .param("payload", "second"))
                .andExpect(status().is3xxRedirection());

        await().atMost(Duration.ofSeconds(15)).untilAsserted(() -> {
            List<DistributionUiStateService.EventView> views = stateService.pageData().events().stream()
                    .filter(event -> event.eventKey().equals("order-777"))
                    .toList();
            assertThat(views).hasSize(2);
            assertThat(views).extracting(DistributionUiStateService.EventView::partition)
                    .containsOnly(views.get(0).partition());
        });

        mockMvc.perform(get("/blocks/kafka-distribution-model"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("order-777")))
                .andExpect(content().string(containsString("Partition")));
    }
}
