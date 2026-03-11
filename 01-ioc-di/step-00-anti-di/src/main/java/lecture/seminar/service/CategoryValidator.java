
package lecture.seminar.service;

public class CategoryValidator {

    public void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is blank");
        }
        if (name.length() > 30) {
            throw new IllegalArgumentException("Name too long");
        }
    }
}
