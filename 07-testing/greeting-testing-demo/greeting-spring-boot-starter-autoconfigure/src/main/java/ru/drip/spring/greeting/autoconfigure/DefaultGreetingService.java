package ru.drip.spring.greeting.autoconfigure;

import java.util.Locale;

final class DefaultGreetingService implements GreetingService {

    private final GreetingProperties properties;

    DefaultGreetingService(GreetingProperties properties) {
        this.properties = properties;
    }

    @Override
    public String greet() {
        String name = properties.getName();
        String language = properties.getLanguage();

        if (language == null) {
            language = "en";
        }

        language = language.toLowerCase(Locale.ROOT);

        if (language.startsWith("ru")) {
            return "Привет, " + name + "!";
        }

        return "Hello, " + name + "!";
    }
}

