package edu.course.webclient.dto;

public record CourseDto(
        String id,
        String title,
        int durationHours,
        CourseStatus status
) {
}

