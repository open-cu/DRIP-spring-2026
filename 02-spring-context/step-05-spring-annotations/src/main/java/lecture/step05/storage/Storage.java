package lecture.step05.storage;

import lecture.step05.domain.Category;

import java.util.List;

public interface Storage {
    void insert(Category category);
    List<Category> findAll();
    boolean deleteById(long id);
}
