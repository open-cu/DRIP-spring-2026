package edu.course.provider;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Course Provider API",
                version = "1.0.0",
                description = "Учебный API для демонстрации RestTemplate, RestClient, WebClient и OpenAPI",
                contact = @Contact(name = "Spring Course Team", email = "course-team@example.edu")
        ),
        servers = @Server(url = "http://localhost:8081", description = "Local demo server")
)
@SpringBootApplication
public class ProviderApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApiApplication.class, args);
    }
}

