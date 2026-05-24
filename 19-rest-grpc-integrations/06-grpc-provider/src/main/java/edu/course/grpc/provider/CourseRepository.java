package edu.course.grpc.provider;

import edu.course.grpc.proto.Course;
import edu.course.grpc.proto.GrpcCourseStatus;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository {

    private final Map<String, Course> courses = new ConcurrentHashMap<>();

    public CourseRepository() {
        save(course("spring-rest", "Spring HTTP Clients", 18, GrpcCourseStatus.GRPC_COURSE_STATUS_OPEN));
        save(course("spring-openapi", "Spring and OpenAPI", 12, GrpcCourseStatus.GRPC_COURSE_STATUS_OPEN));
        save(course("spring-grpc", "Spring Boot and gRPC", 14, GrpcCourseStatus.GRPC_COURSE_STATUS_OPEN));
        save(course("legacy-integrations", "Legacy Integrations with RestTemplate", 8, GrpcCourseStatus.GRPC_COURSE_STATUS_FULL));
    }

    public List<Course> findAll() {
        return courses.values().stream()
                .sorted(Comparator.comparing(Course::getId))
                .toList();
    }

    public Optional<Course> findById(String id) {
        return Optional.ofNullable(courses.get(id));
    }

    private void save(Course course) {
        courses.put(course.getId(), course);
    }

    private Course course(String id, String title, int durationHours, GrpcCourseStatus status) {
        return Course.newBuilder()
                .setId(id)
                .setTitle(title)
                .setDurationHours(durationHours)
                .setStatus(status)
                .build();
    }
}

