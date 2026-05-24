package edu.course.provider.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Courses", description = "Каталог курсов")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Для клиента контрактом становятся не Java-методы, а HTTP method, path,
    // DTO ответа и описанные status codes.
    @Operation(summary = "Получить список курсов")
    @ApiResponse(responseCode = "200", description = "Список курсов успешно получен")
    @GetMapping
    public List<CourseDto> findAll() {
        return courseRepository.findAll();
    }

    @Operation(summary = "Получить курс по идентификатору")
    @ApiResponse(responseCode = "200", description = "Курс найден")
    @ApiResponse(responseCode = "404", description = "Курс не найден")
    @GetMapping("/{id}")
    public CourseDto findById(
            @Parameter(description = "Идентификатор курса", example = "spring-rest")
            @PathVariable String id
    ) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }
}
