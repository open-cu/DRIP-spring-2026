package demo.rabbitmqdemo.slides.slide23;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.rabbitmqdemo.core.model.OrderRequest;
import demo.rabbitmqdemo.core.model.OrderResponse;
import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TDD-кейс №4: Request-Reply с корреляцией")
@ActiveProfiles("slide23")
@AutoConfigureMockMvc
@SpringBootTest
class Slide23RpcRequestReplyIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    void clean() {
        purgeQueues(rabbitAdmin, "order.query.queue", "order.responses");
    }

    @Test
    void rpcReturnsOrderResponse() throws Exception {
        OrderRequest request = new OrderRequest(42L);

        String body = mockMvc.perform(post("/api/slide23/rpc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponse response = objectMapper.readValue(body, OrderResponse.class);
        assertEquals(42L, response.id());
        assertEquals("FOUND", response.status());
    }
}
