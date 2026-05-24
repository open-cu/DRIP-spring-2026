package edu.course.restclient.dto;

public record EnrollmentRequest(
        String courseId,
        String studentEmail
) {
}

