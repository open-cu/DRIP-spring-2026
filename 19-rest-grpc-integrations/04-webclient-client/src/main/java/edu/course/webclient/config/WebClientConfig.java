package edu.course.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient courseWebClient(WebClient.Builder builder, ProviderProperties properties) {
        return builder
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Demo-Client", "WebClient")
                .filter(logRequest())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (request, next) -> {
            System.out.println("WebClient request: " + request.method() + " " + request.url());
            // Filter играет роль client-side middleware: здесь удобно добавлять
            // tracing, auth headers, metrics и безопасное логирование.
            return next.exchange(request)
                    .flatMap(response -> {
                        System.out.println("WebClient response: " + response.statusCode());
                        return Mono.just(response);
                    });
        };
    }
}
