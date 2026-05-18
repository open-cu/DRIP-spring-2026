package edu.course.grpc.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "course.grpc")
public record CourseGrpcProperties(String host, int port) {

    public CourseGrpcProperties {
        if (host == null || host.isBlank()) {
            host = "localhost";
        }
        if (port == 0) {
            port = 9090;
        }
    }
}

