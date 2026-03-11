package lecture.step05.service.validator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class StrictCategoryValidator implements CategoryValidator {

    @Override
    public void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is blank");
        }
        if (name.length() > 30) {
            throw new IllegalArgumentException("Name too long");
        }
    }
}
