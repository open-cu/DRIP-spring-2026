package lecture.step05.service;

import lecture.step05.domain.Category;
import lecture.step05.repo.CategoryRepository;
import lecture.step05.service.validator.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public class CategoryService {

    private final CategoryRepository repo;
    private final CategoryValidator validator;

    @Autowired // не обязательно, если конструктор один
    public CategoryService(CategoryRepository repo, CategoryValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public long create(String name) {
        validator.validate(name);
        return repo.insert(name);
    }
    public List<Category> listAll() {
        return repo.findAll();
    }

    public boolean delete(long id) {
        return repo.deleteById(id);
    }

}
