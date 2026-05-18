package edu.course.webclient.dto;

public record EnrollmentRequest(
        String courseId,
        String studentEmail
) {
}

