package edu.course.webclient.http;

public class CourseApiException extends RuntimeException {

    private final int statusCode;
    private final String responseBody;

    public CourseApiException(int statusCode, String responseBody) {
        super("Course API returned status " + statusCode);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int statusCode() {
        return statusCode;
    }

    public String responseBody() {
        return responseBody;
    }
}

