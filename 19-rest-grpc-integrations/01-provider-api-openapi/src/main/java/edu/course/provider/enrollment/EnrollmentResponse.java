package edu.course.provider.enrollment;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

@Schema(name = "EnrollmentResponse", description = "Результат записи студента на курс")
public record EnrollmentResponse(
        @Schema(description = "Идентификатор записи", example = "enr-7f3c2a")
        String enrollmentId,

        @Schema(description = "Идентификатор курса", example = "spring-rest")
        String courseId,

        @Schema(description = "Email студента", example = "student@example.edu")
        String studentEmail,

        @Schema(description = "Момент создания записи")
        OffsetDateTime createdAt
) {
}

