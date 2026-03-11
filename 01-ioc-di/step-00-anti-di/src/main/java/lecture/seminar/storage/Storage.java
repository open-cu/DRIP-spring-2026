
package lecture.seminar.storage;


import lecture.seminar.domain.Category;

import java.util.List;

public interface Storage {
    void insert(Category category);
    List<Category> findAll();
    boolean deleteById(long id);
}