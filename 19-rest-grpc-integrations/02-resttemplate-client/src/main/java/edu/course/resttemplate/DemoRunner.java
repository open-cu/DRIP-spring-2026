package edu.course.resttemplate;

import edu.course.resttemplate.dto.CourseDto;
import edu.course.resttemplate.dto.EnrollmentRequest;
import edu.course.resttemplate.dto.EnrollmentResponse;
import edu.course.resttemplate.http.CourseApiException;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DemoRunner implements CommandLineRunner {

    private final RestTemplate restTemplate;

    public DemoRunner(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n--- RestTemplate demo ---");

        // ParameterizedTypeReference сохраняет generic-тип List<CourseDto>,
        // который иначе теряется из-за type erasure.
        ResponseEntity<List<CourseDto>> coursesResponse = restTemplate.exchange(
                "/api/courses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CourseDto>>() {
                }
        );
        List<CourseDto> courses = Optional.ofNullable(coursesResponse.getBody()).orElse(List.of());
        System.out.println("Courses: " + courses);

        CourseDto course = restTemplate.getForObject(
                "/api/courses/{id}",
                CourseDto.class,
                "spring-rest"
        );
        System.out.println("Single course: " + course);

        EnrollmentResponse enrollment = restTemplate.postForObject(
                "/api/enrollments",
                new EnrollmentRequest("spring-rest", "resttemplate.student@example.edu"),
                EnrollmentResponse.class
        );
        System.out.println("Enrollment: " + enrollment);

        // В учебном примере 404 превращается в exception. В реальном adapter-е
        // конкретный метод мог бы замаппить такой ответ в Optional.empty().
        try {
            restTemplate.getForObject("/api/courses/{id}", CourseDto.class, "missing-course");
        } catch (CourseApiException ex) {
            System.out.println("Handled error: status=" + ex.statusCode());
            System.out.println("Error body: " + ex.responseBody());
        }
    }
}
