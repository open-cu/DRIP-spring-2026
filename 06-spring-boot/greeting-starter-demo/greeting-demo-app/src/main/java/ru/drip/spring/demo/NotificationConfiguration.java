package ru.drip.spring.demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class NotificationConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "feature", name = "notify", havingValue = "true")
    public NotificationService realNotificationService() {
        return message -> System.out.println("[NOTIFY] " + message);
    }

    @Bean
    @Profile("dev")
    @ConditionalOnProperty(prefix = "feature", name = "notify", havingValue = "false", matchIfMissing = true)
    public NotificationService devNoopNotificationService() {
        return message -> System.out.println("[DEV][NOOP notify] " + message);
    }

    @Bean
    @Profile("!dev")
    @ConditionalOnProperty(prefix = "feature", name = "notify", havingValue = "false", matchIfMissing = true)
    public NotificationService noopNotificationService() {
        return message -> {
        };
    }
}
