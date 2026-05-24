package demo.rabbitmqdemo.blocks.springcore;

import java.time.Duration;

import demo.rabbitmqdemo.config.blocks.springcore.TopologyConfig;
import demo.rabbitmqdemo.testsupport.RequiresDocker;
import demo.rabbitmqdemo.testsupport.TestRabbitProperties;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Блок spring-core: UI-сценарий создать -> обработать -> получить RPC-статус")
@RequiresDocker
@AutoConfigureMockMvc
@SpringBootTest
class SpringCoreUiFlowIntegrationTest {
    private static final Duration UI_AWAIT_TIMEOUT = Duration.ofSeconds(30);

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        TestRabbitProperties.register(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        rabbitAdmin.initialize();
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_ORDERS, false);
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_TEXT_ORDERS, false);
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_RPC, false);
    }

    @Test
    void uiFlowShowsProcessedAndRpcFound() throws Exception {
        mockMvc.perform(post("/blocks/spring-core/orders/defaults")
                        .param("text", "строка через defaults"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/spring-core"));

        Awaitility.await()
                .atMost(UI_AWAIT_TIMEOUT)
                .untilAsserted(() -> mockMvc.perform(get("/blocks/spring-core"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(org.hamcrest.Matchers.containsString("строка через defaults"))));

        mockMvc.perform(post("/blocks/spring-core/orders")
                        .param("description", "Заказ из блока"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/spring-core"));

        Awaitility.await()
                .atMost(UI_AWAIT_TIMEOUT)
                .untilAsserted(() -> mockMvc.perform(get("/blocks/spring-core"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(org.hamcrest.Matchers.containsString("ОБРАБОТАН"))));

        mockMvc.perform(post("/blocks/spring-core/rpc")
                        .param("orderId", "2001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/spring-core"));

        Awaitility.await()
                .atMost(UI_AWAIT_TIMEOUT)
                .untilAsserted(() -> mockMvc.perform(get("/blocks/spring-core"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(org.hamcrest.Matchers.containsString("НАЙДЕН"))));
    }

    @Test
    void blankOrderDescriptionIsIgnored() throws Exception {
        mockMvc.perform(post("/blocks/spring-core/orders")
                        .param("description", "   "))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/spring-core"));

        mockMvc.perform(get("/blocks/spring-core"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Заказов пока нет")));
    }
}
