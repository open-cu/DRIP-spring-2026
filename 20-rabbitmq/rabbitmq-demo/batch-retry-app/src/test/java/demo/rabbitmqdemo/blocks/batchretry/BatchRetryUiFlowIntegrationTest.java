package demo.rabbitmqdemo.blocks.batchretry;

import java.time.Duration;

import demo.rabbitmqdemo.testsupport.RequiresDocker;
import demo.rabbitmqdemo.testsupport.TestRabbitProperties;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Блок batch-retry: UI-сценарий статистики batch и ошибки после повторов")
@RequiresDocker
@AutoConfigureMockMvc
@SpringBootTest
class BatchRetryUiFlowIntegrationTest {

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        TestRabbitProperties.register(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void uiShowsBatchProgressAndRetryFailure() throws Exception {
        mockMvc.perform(post("/blocks/batch-retry/publish-batch")
                        .param("count", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/batch-retry"));

        Awaitility.await()
                .atMost(Duration.ofSeconds(8))
                .untilAsserted(() -> mockMvc.perform(get("/blocks/batch-retry"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("3"))));

        mockMvc.perform(post("/blocks/batch-retry/publish-retry")
                        .param("orderId", "1001")
                        .param("description", "ошибка повтора")
                        .param("failForDemo", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/batch-retry?orderId=1001"));

        Awaitility.await()
                .atMost(Duration.ofSeconds(8))
                .untilAsserted(() -> mockMvc.perform(get("/blocks/batch-retry").param("orderId", "1001"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("ОШИБКА_ПОСЛЕ_ПОВТОРОВ"))));
    }

    @Test
    void zeroBatchSizeDoesNotChangeStats() throws Exception {
        mockMvc.perform(post("/blocks/batch-retry/reset"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/batch-retry"));

        mockMvc.perform(post("/blocks/batch-retry/publish-batch")
                        .param("count", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/batch-retry"));

        mockMvc.perform(get("/blocks/batch-retry"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")));
    }
}
