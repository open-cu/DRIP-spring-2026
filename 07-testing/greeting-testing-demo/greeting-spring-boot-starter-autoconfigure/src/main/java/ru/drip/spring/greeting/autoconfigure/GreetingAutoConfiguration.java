package ru.drip.spring.greeting.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@AutoConfiguration
@EnableConfigurationProperties(GreetingProperties.class)
public class GreetingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GreetingService greetingService(GreetingProperties properties) {
        return new DefaultGreetingService(properties);
    }

    @Bean
    @Profile("dev")
    @ConditionalOnProperty(prefix = "greeting", name = "dev-loud", havingValue = "true", matchIfMissing = true)
    public DevBanner devBanner() {
        return new DevBanner();
    }
}

