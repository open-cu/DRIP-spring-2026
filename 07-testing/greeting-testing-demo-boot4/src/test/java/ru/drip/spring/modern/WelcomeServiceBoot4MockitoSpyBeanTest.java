package ru.drip.spring.modern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(
        classes = ModernDemoApplication.class,
        properties = {
                "demo.student=Spy Student"
        }
)
class WelcomeServiceBoot4MockitoSpyBeanTest {

    @Autowired
    private WelcomeService welcomeService;

    @MockitoSpyBean
    private GreetingService greetingService;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void shouldWrapRealBeanWithMockitoSpyBean() {
        reset(greetingService, notificationService);

        String welcome = welcomeService.buildWelcomeMessage();
        welcomeService.sendWelcomeNotification(welcome);

        assertThat(welcome).isEqualTo("Добро пожаловать! Привет, Spy Student!");
        verify(greetingService).greet();
        verify(notificationService).notify("Добро пожаловать! Привет, Spy Student!");
    }
}
