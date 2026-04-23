package ru.drip.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.StaticMessageSource;

import ru.drip.spring.greeting.autoconfigure.GreetingService;

@ExtendWith(MockitoExtension.class)
class WelcomeServiceTest {

    @Mock
    private GreetingService greetingService;

    @Mock
    private NotificationService notificationService;

    @Test
    void shouldBuildWelcomeMessageWithoutSpringContext() {
        DemoProperties properties = new DemoProperties();
        properties.setLocale("ru");

        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("app.welcome", Locale.forLanguageTag("ru"), "Добро пожаловать! {0}");

        when(greetingService.greet()).thenReturn("Привет, unit test!");

        WelcomeService service = new WelcomeService(greetingService, messageSource, properties, notificationService);

        assertThat(service.buildWelcomeMessage()).isEqualTo("Добро пожаловать! Привет, unit test!");
    }

    @Test
    void shouldDelegateNotificationToDependency() {
        DemoProperties properties = new DemoProperties();
        StaticMessageSource messageSource = new StaticMessageSource();
        WelcomeService service = new WelcomeService(greetingService, messageSource, properties, notificationService);

        service.sendWelcomeNotification("Hello from unit");

        verify(notificationService).notify("Hello from unit");
    }
}
