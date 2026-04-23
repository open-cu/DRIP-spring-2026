package ru.drip.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DemoApplication.class)
class WelcomeServiceSpringBootTest {

    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private DemoProperties properties;

    @MockBean
    private NotificationService notificationService;

    @Test
    void shouldUseTestApplicationYamlAndReplaceNotificationBeanInsideBootContext() {
        String welcome = welcomeService.buildWelcomeMessage();

        assertThat(properties.getLocale()).isEqualTo("ru");
        assertThat(properties.getMeta()).containsEntry("source", "test-resources");
        assertThat(welcome).isEqualTo("Добро пожаловать! Привет, Test Student!");

        reset(notificationService);
        welcomeService.sendWelcomeNotification(welcome);

        verify(notificationService).notify("Добро пожаловать! Привет, Test Student!");
    }
}
