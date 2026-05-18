package edu.course.provider.enrollment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "EnrollmentRequest", description = "Запрос на запись студента на курс")
public record EnrollmentRequest(
        @Schema(description = "Идентификатор курса", example = "spring-rest")
        @NotBlank
        String courseId,

        @Schema(description = "Email студента", example = "student@example.edu")
        @Email
        @NotBlank
        String studentEmail
) {
}

