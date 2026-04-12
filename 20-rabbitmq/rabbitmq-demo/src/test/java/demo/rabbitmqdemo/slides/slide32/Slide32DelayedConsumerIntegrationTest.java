package demo.rabbitmqdemo.slides.slide32;

import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import demo.rabbitmqdemo.config.slide32.Slide32DelayedTopologyConfig;
import demo.rabbitmqdemo.slides.slide32.Slide32TestDelayedTopologyConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TDD-кейс №10: Consumer получает отложенные сообщения")
@ActiveProfiles("slide32")
@AutoConfigureMockMvc
@RabbitListenerTest(capture = true)
@SpringBootTest
@Import({Slide32TestDelayedTopologyConfig.class, Slide32DelayedTopologyConfig.class})
class Slide32DelayedConsumerIntegrationTest extends AbstractRabbitMqIT {

    private static final String LISTENER_ID = "slide32DelayedOrderConsumer";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitListenerTestHarness harness;

    @BeforeEach
    void clean() {
        purgeQueues(rabbitAdmin, "delayed.5s.queue", "process.queue");
    }

    @Test
    void consumerReceivesMessageOnlyAfterDelay() throws Exception {
        Order order = new Order(11L, "Delayed order for consumer");

        mockMvc.perform(post("/api/slide32/delayed")
                        .queryParam("delaySeconds", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":11,"description":"Delayed order for consumer"}
                                """))
                .andExpect(status().isOk());

        assertNoInvocation(harness, LISTENER_ID, 2);
        RabbitListenerTestHarness.InvocationData data = awaitInvocation(harness, LISTENER_ID, 12);
        assertEquals(order, data.getArguments()[0]);
    }
}
