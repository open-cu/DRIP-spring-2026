package ru.drip.spring.modern;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class WelcomeService {

    private final GreetingService greetingService;
    private final MessageSource messageSource;
    private final DemoProperties properties;
    private final NotificationService notificationService;

    public WelcomeService(
            GreetingService greetingService,
            MessageSource messageSource,
            DemoProperties properties,
            NotificationService notificationService
    ) {
        this.greetingService = greetingService;
        this.messageSource = messageSource;
        this.properties = properties;
        this.notificationService = notificationService;
    }

    public Locale resolveLocale() {
        return Locale.forLanguageTag(properties.getLocale());
    }

    public String buildWelcomeMessage() {
        return messageSource.getMessage(
                "app.welcome",
                new Object[]{greetingService.greet()},
                resolveLocale()
        );
    }

    public void sendWelcomeNotification(String message) {
        notificationService.notify(message);
    }
}
