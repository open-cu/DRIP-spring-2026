package ru.drip.spring.greeting.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "greeting")
public class GreetingProperties {

    /**
     * Имя, которое подставляется в приветствие.
     */
    private String name = "World";

    /**
     * Язык приветствия (en/ru).
     */
    private String language = "en";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

