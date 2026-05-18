package edu.course.webclient;

import edu.course.webclient.config.ProviderProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ProviderProperties.class)
public class WebClientClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebClientClientApplication.class, args);
    }
}

