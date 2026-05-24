package edu.course.webclient.dto;

import java.time.Instant;

public record NotificationEvent(
        String type,
        String message,
        Instant createdAt
) {
}

