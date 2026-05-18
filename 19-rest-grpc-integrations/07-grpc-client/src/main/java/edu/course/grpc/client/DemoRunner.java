package edu.course.grpc.client;

import edu.course.grpc.proto.Course;
import edu.course.grpc.proto.CourseCatalogServiceGrpc;
import edu.course.grpc.proto.CourseEvent;
import edu.course.grpc.proto.GetCourseRequest;
import edu.course.grpc.proto.ListCoursesRequest;
import edu.course.grpc.proto.WatchCourseEventsRequest;
import io.grpc.StatusRuntimeException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoRunner implements CommandLineRunner {

    private final CourseCatalogServiceGrpc.CourseCatalogServiceBlockingStub stub;

    public DemoRunner(CourseCatalogServiceGrpc.CourseCatalogServiceBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n--- gRPC client demo ---");

        // Deadline задается на каждый RPC: вызов без срока жизни может зависнуть
        // дольше, чем это допустимо для вызывающего сценария.
        var courses = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .listCourses(ListCoursesRequest.newBuilder().build())
                .getCoursesList();
        System.out.println("Courses:");
        courses.forEach(course -> System.out.println("  - " + describe(course)));

        Course course = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .getCourse(GetCourseRequest.newBuilder()
                        .setId("spring-grpc")
                        .build());
        System.out.println("Single course: " + describe(course));

        try {
            stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getCourse(GetCourseRequest.newBuilder()
                        .setId("missing-course")
                        .build());
        } catch (StatusRuntimeException ex) {
            // gRPC ошибки приходят как StatusRuntimeException, а не как HTTP status.
            System.out.println("Handled gRPC error: status=" + ex.getStatus().getCode());
            System.out.println("Description: " + ex.getStatus().getDescription());
        }

        System.out.println("First three gRPC stream events:");
        // Blocking stub представляет server stream как Iterator.
        Iterator<CourseEvent> events = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .watchCourseEvents(WatchCourseEventsRequest.newBuilder()
                        .setLimit(3)
                        .build());
        events.forEachRemaining(event -> System.out.println("  - " + event.getMessage()));
    }

    private String describe(Course course) {
        return "%s (%s, %d hours, %s)"
                .formatted(course.getTitle(), course.getId(), course.getDurationHours(), course.getStatus());
    }
}
