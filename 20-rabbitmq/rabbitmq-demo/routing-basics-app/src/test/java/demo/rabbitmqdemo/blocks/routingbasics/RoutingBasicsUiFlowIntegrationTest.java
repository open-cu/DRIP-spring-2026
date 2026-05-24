package demo.rabbitmqdemo.blocks.routingbasics;

import java.time.Duration;

import demo.rabbitmqdemo.testsupport.RequiresDocker;
import demo.rabbitmqdemo.testsupport.TestRabbitProperties;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Блок routing-basics: UI отправляет сообщения в очереди маршрутизации")
@RequiresDocker
@AutoConfigureMockMvc
@SpringBootTest
class RoutingBasicsUiFlowIntegrationTest {
    private static final String DIRECT_ORDERS_QUEUE = "orders.queue";
    private static final String DIRECT_BILLING_QUEUE = "billing.queue";
    private static final String TOPIC_ORDERS_QUEUE = "orders.queue";
    private static final String TOPIC_NOTIFICATIONS_QUEUE = "notifications.queue";
    private static final String FANOUT_MOSCOW_QUEUE = "notifications.moscow.queue";
    private static final String FANOUT_PSCOV_QUEUE = "notifications.pscov.queue";
    private static final String FANOUT_TVER_QUEUE = "notifications.tver.queue";
    private static final String FANOUT_ELISTA_QUEUE = "notifications.elista.queue";
    private static final String HEADERS_HIGH_QUEUE = "orders.headers.high.queue";
    private static final String HEADERS_LOW_QUEUE = "orders.headers.low.queue";

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        TestRabbitProperties.register(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        rabbitAdmin.initialize();
        for (String queueName : new String[]{
                DIRECT_ORDERS_QUEUE,
                DIRECT_BILLING_QUEUE,
                TOPIC_ORDERS_QUEUE,
                TOPIC_NOTIFICATIONS_QUEUE,
                FANOUT_MOSCOW_QUEUE,
                FANOUT_PSCOV_QUEUE,
                FANOUT_TVER_QUEUE,
                FANOUT_ELISTA_QUEUE,
                HEADERS_HIGH_QUEUE,
                HEADERS_LOW_QUEUE
        }) {
            rabbitAdmin.purgeQueue(queueName, false);
        }
    }

    @Test
    void directAndHeadersMessagesReachExpectedQueues() throws Exception {
        mockMvc.perform(post("/blocks/routing-basics/send/direct")
                        .param("routingKey", "order.created")
                        .param("description", "прямой маршрут"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/routing-basics"));

        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    Message message = rabbitTemplate.receive(DIRECT_ORDERS_QUEUE);
                    assertNotNull(message);
                });

        mockMvc.perform(post("/blocks/routing-basics/send/headers")
                        .param("priority", "HIGH")
                        .param("description", "маршрут по заголовку"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/routing-basics"));

        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    Message message = rabbitTemplate.receive(HEADERS_HIGH_QUEUE);
                    assertNotNull(message);
                });
    }

    @Test
    void blankDirectMessageIsIgnored() throws Exception {
        mockMvc.perform(post("/blocks/routing-basics/send/direct")
                        .param("routingKey", "order.created")
                        .param("description", "   "))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blocks/routing-basics"));

        Awaitility.await()
                .atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> {
                    Message message = rabbitTemplate.receive(DIRECT_ORDERS_QUEUE);
                    org.junit.jupiter.api.Assertions.assertNull(message);
                });
    }
}
