package edu.course.resttemplate;

import edu.course.resttemplate.config.ProviderProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ProviderProperties.class)
public class RestTemplateClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestTemplateClientApplication.class, args);
    }
}

