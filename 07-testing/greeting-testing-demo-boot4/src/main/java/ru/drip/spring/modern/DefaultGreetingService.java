package ru.drip.spring.modern;

import org.springframework.stereotype.Service;

@Service
public class DefaultGreetingService implements GreetingService {

    private final DemoProperties properties;

    public DefaultGreetingService(DemoProperties properties) {
        this.properties = properties;
    }

    @Override
    public String greet() {
        if ("ru".equals(properties.getLocale())) {
            return "Привет, " + properties.getStudent() + "!";
        }
        return "Hello, " + properties.getStudent() + "!";
    }
}
