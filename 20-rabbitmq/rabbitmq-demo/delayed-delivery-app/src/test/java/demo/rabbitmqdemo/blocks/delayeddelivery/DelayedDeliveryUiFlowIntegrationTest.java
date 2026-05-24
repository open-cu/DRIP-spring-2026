package demo.rabbitmqdemo.blocks.delayeddelivery;

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

@DisplayName("Блок delayed-delivery: UI-сценарий ожидание -> обработка")
@RequiresDocker
@AutoConfigureMockMvc
@SpringBootTest
class DelayedDeliveryUiFlowIntegrationTest {

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        TestRabbitProperties.register(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void uiShowsPendingThenProcessed() throws Exception {
        mockMvc.perform(post("/blocks/delayed-delivery/orders")
                        .param("description", "отложенный заказ")
                        .param("delaySeconds", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/delayed-delivery"));

        mockMvc.perform(get("/blocks/delayed-delivery"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ОЖИДАЕТ")));

        Awaitility.await()
                .atMost(Duration.ofSeconds(12))
                .untilAsserted(() -> mockMvc.perform(get("/blocks/delayed-delivery"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("ОБРАБОТАН"))));
    }

    @Test
    void unsupportedDelayDoesNotCreateOrder() throws Exception {
        mockMvc.perform(post("/blocks/delayed-delivery/orders")
                        .param("description", "некорректная задержка")
                        .param("delaySeconds", "7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/delayed-delivery"));

        mockMvc.perform(get("/blocks/delayed-delivery"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("некорректная задержка"))));
    }
}
