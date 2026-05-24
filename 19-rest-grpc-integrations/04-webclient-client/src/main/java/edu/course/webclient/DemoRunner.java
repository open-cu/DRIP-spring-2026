package edu.course.webclient;

import edu.course.webclient.dto.CourseDto;
import edu.course.webclient.dto.EnrollmentRequest;
import edu.course.webclient.dto.EnrollmentResponse;
import edu.course.webclient.dto.NotificationEvent;
import edu.course.webclient.http.CourseApiException;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DemoRunner implements CommandLineRunner {

    private final WebClient webClient;

    public DemoRunner(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n--- WebClient demo ---");

        // block(...) оставлен только на границе CLI-demo. В WebFlux endpoint-е
        // обычно вернули бы Mono/Flux дальше, не блокируя поток.
        List<CourseDto> courses = webClient.get()
                .uri("/api/courses")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> toCourseApiException(response.statusCode(), response))
                .bodyToFlux(CourseDto.class)
                .collectList()
                .block(Duration.ofSeconds(5));
        System.out.println("Courses: " + courses);

        Mono<CourseDto> courseMono = webClient.get()
                .uri("/api/courses/{id}", "spring-rest")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> toCourseApiException(response.statusCode(), response))
                .bodyToMono(CourseDto.class);

        Mono<EnrollmentResponse> enrollmentMono = webClient.post()
                .uri("/api/enrollments")
                .bodyValue(new EnrollmentRequest("reactive-streams", "webclient.student@example.edu"))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> toCourseApiException(response.statusCode(), response))
                .bodyToMono(EnrollmentResponse.class);

        // Mono.zip запускает независимые операции как композицию будущих значений.
        String summary = Mono.zip(courseMono, enrollmentMono)
                .map(tuple -> "Course '%s', enrollment '%s'"
                        .formatted(tuple.getT1().title(), tuple.getT2().enrollmentId()))
                .block(Duration.ofSeconds(5));
        System.out.println("Composed result: " + summary);

        try {
            webClient.get()
                    .uri("/api/courses/{id}", "missing-course")
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> toCourseApiException(response.statusCode(), response))
                    .bodyToMono(CourseDto.class)
                    .block(Duration.ofSeconds(5));
        } catch (CourseApiException ex) {
            System.out.println("Handled error: status=" + ex.statusCode());
            System.out.println("Error body: " + ex.responseBody());
        }

        System.out.println("First three SSE events:");
        webClient.get()
                .uri("/api/notifications")
                .retrieve()
                // SSE stream читается как Flux: каждый event приходит отдельно.
                .bodyToFlux(NotificationEvent.class)
                .take(3)
                .doOnNext(event -> System.out.println("Event: " + event))
                .blockLast(Duration.ofSeconds(10));
    }

    private Mono<? extends Throwable> toCourseApiException(
            HttpStatusCode statusCode,
            org.springframework.web.reactive.function.client.ClientResponse response
    ) {
        // Error body тоже читается реактивно: exception создается внутри pipeline-а.
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> new CourseApiException(statusCode.value(), body));
    }
}
