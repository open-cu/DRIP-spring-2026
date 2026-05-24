package edu.course.openapi;

import edu.course.openapi.config.OpenApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenApiProperties.class)
public class OpenApiContractInspectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiContractInspectorApplication.class, args);
    }
}

