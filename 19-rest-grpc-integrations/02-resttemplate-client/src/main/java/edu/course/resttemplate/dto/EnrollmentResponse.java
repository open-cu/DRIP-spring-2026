package edu.course.resttemplate.dto;

import java.time.OffsetDateTime;

public record EnrollmentResponse(
        String enrollmentId,
        String courseId,
        String studentEmail,
        OffsetDateTime createdAt
) {
}

