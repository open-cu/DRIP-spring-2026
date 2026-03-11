package lecture.step04.repo;


import lecture.step04.domain.Category;

import java.util.List;

public interface CategoryRepository {
    long insert(String name);

    List<Category> findAll();

    boolean deleteById(long id);
}
