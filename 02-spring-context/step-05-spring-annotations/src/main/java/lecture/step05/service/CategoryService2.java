package lecture.step05.service;

import lecture.step05.repo.CategoryRepository;
import lecture.step05.service.validator.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService2 {
    private CategoryRepository repo;
    private CategoryValidator validator;

    @Autowired
    public void setRepo(CategoryRepository repo) {
        this.repo = repo;
    }

    @Autowired
    public void setValidator(CategoryValidator validator) {
        this.validator = validator;
    }
}


