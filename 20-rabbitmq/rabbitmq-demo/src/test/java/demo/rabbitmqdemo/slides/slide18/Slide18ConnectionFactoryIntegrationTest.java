package demo.rabbitmqdemo.slides.slide18;

import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.DisplayName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TDD-кейс №1: ConnectionFactory подключается к RabbitMQ")
@ActiveProfiles("slide18")
@AutoConfigureMockMvc
@SpringBootTest
class Slide18ConnectionFactoryIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void connectionFactoryConnectsToRabbitMq() throws Exception {
        mockMvc.perform(get("/api/slide18/rabbit/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}
