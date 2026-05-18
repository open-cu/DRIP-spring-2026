package edu.course.provider.course;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус курса")
public enum CourseStatus {
    OPEN,
    FULL,
    ARCHIVED
}

