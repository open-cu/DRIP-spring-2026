package demo.kafkademo.blocks.kafkacore;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;

import demo.kafkademo.testsupport.KafkaContainerSupport;
import demo.kafkademo.testsupport.RequiresDocker;
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
class KafkaCoreUiFlowIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        KafkaContainerSupport.register(registry);
    }

    @Test
    void rootPathAndBlockPathOpenCorePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-core: первый рабочий путь события")));

        mockMvc.perform(get("/blocks/kafka-core"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-core: первый рабочий путь события")));
    }

    @Test
    void streamsPageOpensInsideCoreApp() throws Exception {
        mockMvc.perform(get("/blocks/kafka-streams"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Блок kafka-streams: topology внутри kafka-core-app")))
                .andExpect(content().string(containsString("orders.stream.input")))
                .andExpect(content().string(containsString("orders.stream.result")));
    }

    @Test
    void orderCreatedEventIsProcessedByListener() throws Exception {
        mockMvc.perform(post("/blocks/kafka-core/orders").param("description", "core заказ"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/kafka-core"));

        await().atMost(Duration.ofSeconds(15)).untilAsserted(() ->
                mockMvc.perform(get("/blocks/kafka-core"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("core заказ")))
                        .andExpect(content().string(containsString("ОБРАБОТАН")))
        );
    }

    @Test
    void streamsTopologyWritesResultToUiView() throws Exception {
        mockMvc.perform(post("/blocks/kafka-streams/events").param("description", "stream заказ"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/kafka-streams"));

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() ->
                mockMvc.perform(get("/blocks/kafka-streams"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("stream заказ")))
                        .andExpect(content().string(containsString("STREAM_ACCEPTED")))
        );
    }
}
