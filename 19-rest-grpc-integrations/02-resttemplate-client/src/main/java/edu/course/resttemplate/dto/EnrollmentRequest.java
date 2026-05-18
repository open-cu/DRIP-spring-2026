package edu.course.resttemplate.dto;

public record EnrollmentRequest(
        String courseId,
        String studentEmail
) {
}

