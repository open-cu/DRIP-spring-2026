package edu.course.provider.enrollment;

import edu.course.provider.course.CourseNotFoundException;
import edu.course.provider.course.CourseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Enrollments", description = "Запись студентов на курсы")
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final CourseRepository courseRepository;

    public EnrollmentController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Validation, request body и 201 Created тоже являются частью внешнего API,
    // поэтому они должны быть отражены в OpenAPI и клиентских примерах.
    @Operation(summary = "Записать студента на курс")
    @ApiResponse(responseCode = "201", description = "Студент записан на курс")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    @ApiResponse(responseCode = "404", description = "Курс не найден")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse enroll(@Valid @RequestBody EnrollmentRequest request) {
        if (!courseRepository.existsById(request.courseId())) {
            throw new CourseNotFoundException(request.courseId());
        }
        return new EnrollmentResponse(
                "enr-" + UUID.randomUUID().toString().substring(0, 8),
                request.courseId(),
                request.studentEmail(),
                OffsetDateTime.now()
        );
    }
}
