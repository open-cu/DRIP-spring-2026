package edu.course.grpc.provider;

import edu.course.grpc.proto.Course;
import edu.course.grpc.proto.CourseCatalogServiceGrpc;
import edu.course.grpc.proto.CourseEvent;
import edu.course.grpc.proto.GetCourseRequest;
import edu.course.grpc.proto.ListCoursesRequest;
import edu.course.grpc.proto.ListCoursesResponse;
import edu.course.grpc.proto.WatchCourseEventsRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class CourseCatalogGrpcService extends CourseCatalogServiceGrpc.CourseCatalogServiceImplBase {

    private final CourseRepository courseRepository;

    public CourseCatalogGrpcService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void getCourse(GetCourseRequest request, StreamObserver<Course> responseObserver) {
        // StreamObserver управляет жизненным циклом ответа: onNext + onCompleted
        // для успеха или onError со статусом gRPC для ошибки.
        courseRepository.findById(request.getId())
                .ifPresentOrElse(course -> {
                    responseObserver.onNext(course);
                    responseObserver.onCompleted();
                }, () -> responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Course '%s' was not found".formatted(request.getId()))
                        .asRuntimeException()));
    }

    @Override
    public void listCourses(ListCoursesRequest request, StreamObserver<ListCoursesResponse> responseObserver) {
        ListCoursesResponse response = ListCoursesResponse.newBuilder()
                .addAllCourses(courseRepository.findAll())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void watchCourseEvents(
            WatchCourseEventsRequest request,
            StreamObserver<CourseEvent> responseObserver
    ) {
        int limit = request.getLimit() <= 0 ? 3 : request.getLimit();
        try {
            // Server streaming: один request открывает поток нескольких responses.
            for (int i = 1; i <= limit; i++) {
                CourseEvent event = CourseEvent.newBuilder()
                        .setType("COURSE_UPDATED")
                        .setCourseId("spring-grpc")
                        .setMessage("gRPC course event #" + i)
                        .setCreatedAtEpochMillis(Instant.now().toEpochMilli())
                        .build();
                responseObserver.onNext(event);
                Thread.sleep(500);
            }
            responseObserver.onCompleted();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            responseObserver.onError(Status.CANCELLED
                    .withDescription("Event stream was interrupted")
                    .asRuntimeException());
        }
    }
}
