package edu.course.resttemplate.config;

import edu.course.resttemplate.http.CourseApiErrorHandler;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder, ProviderProperties properties) {
        // Один bean хранит техническую политику интеграции: адрес provider-а,
        // timeouts и mapping ошибок. Вызовы ниже остаются короче и единообразнее.
        return builder
                .rootUri(properties.baseUrl())
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(5))
                .errorHandler(new CourseApiErrorHandler())
                .build();
    }
}
