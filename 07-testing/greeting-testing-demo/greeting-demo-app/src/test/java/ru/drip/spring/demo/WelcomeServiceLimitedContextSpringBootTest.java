package ru.drip.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.StaticMessageSource;

import ru.drip.spring.greeting.autoconfigure.GreetingService;

@SpringBootTest(
        classes = WelcomeServiceLimitedContextSpringBootTest.TestApplication.class,
        properties = {
                "demo.locale=ru",
                "demo.meta.topic=limited-boot-test"
        }
)
class WelcomeServiceLimitedContextSpringBootTest {

    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private RecordingNotificationService notificationService;

    @Autowired
    private DemoProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldStartOnlyExplicitBootTestGraph() {
        String welcome = welcomeService.buildWelcomeMessage();
        welcomeService.sendWelcomeNotification(welcome);

        assertThat(welcome).isEqualTo("Добро пожаловать! Hello from limited Boot!");
        assertThat(notificationService.lastMessage()).isEqualTo("Добро пожаловать! Hello from limited Boot!");
        assertThat(properties.getMeta()).containsEntry("topic", "limited-boot-test");
        assertThat(applicationContext.getBeanNamesForType(WelcomeService.class))
                .containsExactly(WelcomeService.class.getName());
        assertThat(applicationContext.getBeanNamesForType(DemoRunner.class)).isEmpty();
    }

    @SpringBootConfiguration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @EnableConfigurationProperties(DemoProperties.class)
    @Import({WelcomeService.class, StubBeans.class})
    static class TestApplication {
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class StubBeans {

        @Bean
        GreetingService greetingService() {
            return () -> "Hello from limited Boot!";
        }

        @Bean
        MessageSource messageSource() {
            StaticMessageSource messageSource = new StaticMessageSource();
            messageSource.addMessage("app.welcome", Locale.forLanguageTag("ru"), "Добро пожаловать! {0}");
            messageSource.addMessage("app.welcome", Locale.ENGLISH, "Welcome! {0}");
            return messageSource;
        }

        @Bean
        RecordingNotificationService notificationService() {
            return new RecordingNotificationService();
        }
    }

    static class RecordingNotificationService implements NotificationService {

        private String lastMessage;

        @Override
        public void notify(String message) {
            this.lastMessage = message;
        }

        String lastMessage() {
            return lastMessage;
        }
    }
}
