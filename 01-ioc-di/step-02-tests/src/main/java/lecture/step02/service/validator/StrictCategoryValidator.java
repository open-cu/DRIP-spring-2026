package lecture.step02.service.validator;

public class StrictCategoryValidator implements CategoryValidator {

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

        // 3) допустимые символы (буквы/цифры/пробел/дефис)
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            boolean ok = Character.isLetterOrDigit(c) || c == ' ' || c == '-';
            if (!ok) {
                throw new IllegalArgumentException("Invalid char: '" + c + "'");
            }
        }

        // 4) запрет двойных пробелов (необязательно, но часто полезно)
        if (name.contains("  ")) {
            throw new IllegalArgumentException("Name contains double spaces");
        }
    }
}
