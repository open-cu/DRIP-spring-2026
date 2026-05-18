package edu.course.restclient;

import edu.course.restclient.dto.CourseDto;
import edu.course.restclient.dto.EnrollmentRequest;
import edu.course.restclient.dto.EnrollmentResponse;
import edu.course.restclient.http.CourseApiException;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DemoRunner implements CommandLineRunner {

    private final RestClient restClient;

    public DemoRunner(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n--- RestClient demo ---");

        // Сценарии совпадают с RestTemplate-примером, чтобы сравнивать форму API,
        // а не разную бизнес-логику.
        List<CourseDto> courses = restClient.get()
                .uri("/api/courses")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CourseDto>>() {
                });
        System.out.println("Courses: " + courses);

        CourseDto course = restClient.get()
                .uri("/api/courses/{id}", "spring-rest")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(CourseDto.class);
        System.out.println("Single course: " + course);

        EnrollmentResponse enrollment = restClient.post()
                .uri("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new EnrollmentRequest("spring-openapi", "restclient.student@example.edu"))
                .retrieve()
                .body(EnrollmentResponse.class);
        System.out.println("Enrollment: " + enrollment);

        try {
            restClient.get()
                    .uri("/api/courses/{id}", "missing-course")
                    .retrieve()
                    .body(CourseDto.class);
        } catch (CourseApiException ex) {
            System.out.println("Handled error: status=" + ex.statusCode());
            System.out.println("Error body: " + ex.responseBody());
        }
    }
}
