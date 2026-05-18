package edu.course.restclient.config;

import edu.course.restclient.http.CourseApiException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient courseRestClient(RestClient.Builder builder, ProviderProperties properties) {
        // RestClient остается синхронным, но общая политика задается fluent builder-ом:
        // base URL, стандартные headers и единый handler для 4xx/5xx.
        return builder
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Demo-Client", "RestClient")
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    String body = readBody(response.getBody());
                    throw new CourseApiException(response.getStatusCode().value(), body);
                })
                .build();
    }

    private static String readBody(java.io.InputStream body) throws IOException {
        return StreamUtils.copyToString(body, StandardCharsets.UTF_8);
    }
}
