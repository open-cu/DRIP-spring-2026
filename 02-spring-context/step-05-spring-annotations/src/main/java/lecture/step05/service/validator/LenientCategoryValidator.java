package lecture.step05.service.validator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("demo")
public class LenientCategoryValidator implements CategoryValidator {

    @Override
    public void validate(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
    }
}
