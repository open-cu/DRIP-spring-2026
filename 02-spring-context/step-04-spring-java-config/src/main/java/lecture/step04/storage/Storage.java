package lecture.step04.storage;

import lecture.step04.domain.Category;

import java.util.List;

public interface Storage {
    void insert(Category category);
    List<Category> findAll();
    boolean deleteById(long id);
}
