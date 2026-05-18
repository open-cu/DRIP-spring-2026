package edu.course.provider.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "NotificationEvent", description = "Событие учебной платформы")
public record NotificationEvent(
        @Schema(description = "Тип события", example = "COURSE_UPDATED")
        String type,

        @Schema(description = "Текст события", example = "Course spring-rest was updated")
        String message,

        @Schema(description = "Время события")
        Instant createdAt
) {
}

