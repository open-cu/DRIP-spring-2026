package edu.course.provider.course;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Course", description = "Курс учебной платформы")
public record CourseDto(
        @Schema(description = "Стабильный идентификатор курса", example = "spring-rest")
        String id,

        @Schema(description = "Название курса", example = "Spring HTTP Clients")
        String title,

        @Schema(description = "Длительность курса в академических часах", example = "18")
        int durationHours,

        @Schema(description = "Текущий статус курса", example = "OPEN")
        CourseStatus status
) {
}

