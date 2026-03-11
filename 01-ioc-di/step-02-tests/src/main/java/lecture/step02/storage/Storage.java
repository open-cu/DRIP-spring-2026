package lecture.step02.storage;


import lecture.step02.domain.Category;

import java.util.List;

public interface Storage {
    void insert(Category category);
    List<Category> findAll();
    boolean deleteById(long id);
}
