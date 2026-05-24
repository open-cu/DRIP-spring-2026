package ru.drip.spring.modern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(DemoProperties.class)
public class ModernDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModernDemoApplication.class, args);
    }
}
