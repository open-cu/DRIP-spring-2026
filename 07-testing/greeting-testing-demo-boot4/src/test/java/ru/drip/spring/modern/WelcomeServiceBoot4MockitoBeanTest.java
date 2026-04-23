package ru.drip.spring.modern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = ModernDemoApplication.class)
class WelcomeServiceBoot4MockitoBeanTest {

    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private DemoProperties properties;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void shouldUseMockitoBeanInsideBoot4Context() {
        String welcome = welcomeService.buildWelcomeMessage();

        assertThat(properties.getLocale()).isEqualTo("ru");
        assertThat(properties.getMeta()).containsEntry("source", "boot4-test-resources");
        assertThat(welcome).isEqualTo("Добро пожаловать! Привет, Modern Test Student!");

        reset(notificationService);
        welcomeService.sendWelcomeNotification(welcome);

        verify(notificationService).notify("Добро пожаловать! Привет, Modern Test Student!");
    }
}
