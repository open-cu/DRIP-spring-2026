package edu.course.webclient.dto;

import java.time.OffsetDateTime;

public record EnrollmentResponse(
        String enrollmentId,
        String courseId,
        String studentEmail,
        OffsetDateTime createdAt
) {
}

