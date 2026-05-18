package edu.course.restclient.dto;

public record CourseDto(
        String id,
        String title,
        int durationHours,
        CourseStatus status
) {
}

