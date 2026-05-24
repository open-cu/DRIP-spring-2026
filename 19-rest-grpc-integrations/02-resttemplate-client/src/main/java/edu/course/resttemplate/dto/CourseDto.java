package edu.course.resttemplate.dto;

public record CourseDto(
        String id,
        String title,
        int durationHours,
        CourseStatus status
) {
}

