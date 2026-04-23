package ru.drip.spring.modern;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoRunner implements ApplicationRunner {

    private final WelcomeService welcomeService;

    public DemoRunner(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @Override
    public void run(ApplicationArguments args) {
        String welcome = welcomeService.buildWelcomeMessage();
        System.out.println(welcome);
        welcomeService.sendWelcomeNotification(welcome);
    }
}
