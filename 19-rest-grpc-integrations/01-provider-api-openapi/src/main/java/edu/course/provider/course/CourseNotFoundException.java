package edu.course.provider.course;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String id) {
        super("Course '%s' was not found".formatted(id));
    }
}

