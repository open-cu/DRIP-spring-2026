package lecture.step05.service;

import lecture.step05.repo.CategoryRepository;
import lecture.step05.service.validator.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService3 {
    @Autowired
    private CategoryRepository repo;
    @Autowired
    private CategoryValidator validator;
}


