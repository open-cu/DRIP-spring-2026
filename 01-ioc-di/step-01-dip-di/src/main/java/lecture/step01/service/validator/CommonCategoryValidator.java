package lecture.step01.service.validator;

public class CommonCategoryValidator implements CategoryValidator {
    private static final int MAX_LEN = 30;

    @Override
    public void validate(String name) {
        // 1) null / blank
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is blank");
        }

        // 2) ограничение длины
        if (name.length() > MAX_LEN) {
            throw new IllegalArgumentException("Name too long (max=" + MAX_LEN + ")");
        }
    }
}
