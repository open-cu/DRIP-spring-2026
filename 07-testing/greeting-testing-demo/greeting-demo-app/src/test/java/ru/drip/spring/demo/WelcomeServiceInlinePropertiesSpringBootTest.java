package ru.drip.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
        "greeting.name=Inline Student",
        "greeting.language=en",
        "demo.locale=en"
}, classes = DemoApplication.class)
class WelcomeServiceInlinePropertiesSpringBootTest {

    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private DemoProperties properties;

    @MockBean
    private NotificationService notificationService;

    @Test
    void shouldAllowInlinePropertiesToOverrideTestResources() {
        assertThat(properties.getLocale()).isEqualTo("en");
        assertThat(welcomeService.buildWelcomeMessage()).isEqualTo("Welcome! Hello, Inline Student!");
    }
}
