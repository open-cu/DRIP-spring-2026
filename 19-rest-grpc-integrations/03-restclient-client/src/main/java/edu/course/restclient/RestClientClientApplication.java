package edu.course.restclient;

import edu.course.restclient.config.ProviderProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ProviderProperties.class)
public class RestClientClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestClientClientApplication.class, args);
    }
}

