package lecture.step01.service.validator;

public class LenientCategoryValidator implements CategoryValidator {

    @Override
    public void validate(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
    }
}
