package demo.kafkademo.blocks.kafkadelayed;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;

import demo.kafkademo.service.DelayedUiStateService;
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
class KafkaDelayedProcessingUiFlowIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    DelayedUiStateService stateService;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        KafkaContainerSupport.register(registry);
    }

    @BeforeEach
    void resetState() {
        stateService.reset();
    }

    @Test
    void rootPathAndBlockPathOpenDelayedPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-delayed-processing: отложенный заказ")));

        mockMvc.perform(get("/blocks/kafka-delayed-processing"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-delayed-processing: отложенный заказ")));
    }

    @Test
    void delayedOrderMovesFromPendingToProcessed() throws Exception {
        mockMvc.perform(post("/blocks/kafka-delayed-processing/orders")
                        .param("description", "delayed заказ")
                        .param("delaySeconds", "5"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/blocks/kafka-delayed-processing"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("delayed заказ")))
                .andExpect(content().string(containsString("ОЖИДАЕТ")));

        await().atMost(Duration.ofSeconds(15)).untilAsserted(() ->
                assertThat(stateService.pageData().orders())
                        .anySatisfy(order -> {
                            assertThat(order.description()).isEqualTo("delayed заказ");
                            assertThat(order.status()).isEqualTo("ОБРАБОТАН");
                        })
        );
    }

    @Test
    void failingDelayedOrderGoesThroughRetryAndThenDlt() throws Exception {
        mockMvc.perform(post("/blocks/kafka-delayed-processing/orders")
                        .param("description", "fail заказ")
                        .param("delaySeconds", "5"))
                .andExpect(status().is3xxRedirection());

        await().atMost(Duration.ofSeconds(25)).untilAsserted(() ->
                assertThat(stateService.pageData().orders())
                        .anySatisfy(order -> {
                            assertThat(order.description()).isEqualTo("fail заказ");
                            assertThat(order.status()).isEqualTo("DLT");
                        })
        );
    }
}
