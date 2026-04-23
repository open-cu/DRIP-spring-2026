package ru.drip.spring.greeting.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DefaultGreetingServiceTest {

    @Test
    void shouldUseEnglishGreetingByDefault() {
        GreetingProperties properties = new GreetingProperties();
        properties.setName("DRIP");

        DefaultGreetingService service = new DefaultGreetingService(properties);

        assertThat(service.greet()).isEqualTo("Hello, DRIP!");
    }

    @Test
    void shouldSwitchToRussianGreetingForRuLanguage() {
        GreetingProperties properties = new GreetingProperties();
        properties.setName("DRIP");
        properties.setLanguage("ru_RU");

        DefaultGreetingService service = new DefaultGreetingService(properties);

        assertThat(service.greet()).isEqualTo("Привет, DRIP!");
    }
}
