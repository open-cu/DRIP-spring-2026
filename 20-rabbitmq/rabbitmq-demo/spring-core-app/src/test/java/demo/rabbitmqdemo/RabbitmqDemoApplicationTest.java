package demo.rabbitmqdemo;

import demo.rabbitmqdemo.testsupport.RequiresDocker;
import demo.rabbitmqdemo.testsupport.TestRabbitProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiresDocker
@AutoConfigureMockMvc
@SpringBootTest
class RabbitmqDemoApplicationTest {

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        TestRabbitProperties.register(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void appStartsAndMainUiPageIsAvailable() throws Exception {
        mockMvc.perform(get("/blocks/spring-core"))
                .andExpect(status().isOk());
    }
}
