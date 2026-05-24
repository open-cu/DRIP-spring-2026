package edu.course.provider.course;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository {

    private final Map<String, CourseDto> courses = new ConcurrentHashMap<>();

    public CourseRepository() {
        save(new CourseDto("spring-rest", "Spring HTTP Clients", 18, CourseStatus.OPEN));
        save(new CourseDto("spring-openapi", "Spring and OpenAPI", 12, CourseStatus.OPEN));
        save(new CourseDto("legacy-integrations", "Legacy Integrations with RestTemplate", 8, CourseStatus.FULL));
        save(new CourseDto("reactive-streams", "Reactive HTTP with WebClient", 16, CourseStatus.OPEN));
        save(new CourseDto("old-soap-bridge", "Old SOAP Bridge", 6, CourseStatus.ARCHIVED));
    }

    public List<CourseDto> findAll() {
        return courses.values().stream()
                .sorted(Comparator.comparing(CourseDto::id))
                .toList();
    }

    public Optional<CourseDto> findById(String id) {
        return Optional.ofNullable(courses.get(id));
    }

    public boolean existsById(String id) {
        return courses.containsKey(id);
    }

    private void save(CourseDto course) {
        courses.put(course.id(), course);
    }
}

