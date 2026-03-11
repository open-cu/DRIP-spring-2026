package lecture.step01.storage;


import lecture.step01.domain.Category;

import java.util.List;

public interface Storage {
    void insert(Category category);
    List<Category> findAll();
    boolean deleteById(long id);
}
