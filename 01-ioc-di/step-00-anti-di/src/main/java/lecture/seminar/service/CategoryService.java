
package lecture.seminar.service;


import lecture.seminar.domain.Category;
import lecture.seminar.repo.CategoryRepository;

import java.util.List;

public class CategoryService {

    private final CategoryRepository repo = new CategoryRepository(); // anti-DI
    private final CategoryValidator validator = new CategoryValidator(); // anti-DI


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
