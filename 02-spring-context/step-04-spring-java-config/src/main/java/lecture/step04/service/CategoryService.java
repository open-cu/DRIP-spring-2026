package lecture.step04.service;

import lecture.step04.domain.Category;
import lecture.step04.repo.CategoryRepository;
import lecture.step04.service.validator.CategoryValidator;

import java.util.List;

public class CategoryService {

    private final CategoryRepository repo;
    private final List<CategoryValidator> validator;

    public CategoryService(CategoryRepository repo, List<CategoryValidator> validator) {
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