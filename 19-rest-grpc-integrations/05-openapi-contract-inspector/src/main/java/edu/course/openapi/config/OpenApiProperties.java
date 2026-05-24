package edu.course.openapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openapi")
public class OpenApiProperties {

    private String source = "src/main/resources/openapi/course-api.yaml";

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

