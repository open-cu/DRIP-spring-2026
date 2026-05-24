package ru.drip.spring.modern;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
public class NotificationConfiguration {

    @Bean
    @ConditionalOnProperty(name = "feature.notify", havingValue = "true")
    NotificationService realNotificationService() {
        return message -> System.out.println("[NOTIFY] " + message);
    }

    @Bean
    @Profile("dev")
    @ConditionalOnProperty(name = "feature.notify", havingValue = "false", matchIfMissing = true)
    NotificationService devNoopNotificationService() {
        return message -> System.out.println("[DEV][NOOP] " + message);
    }

    @Bean
    @ConditionalOnMissingBean(NotificationService.class)
    NotificationService noopNotificationService() {
        return message -> {
        };
    }
}
