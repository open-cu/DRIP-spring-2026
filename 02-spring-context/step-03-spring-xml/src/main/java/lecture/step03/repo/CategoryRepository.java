package lecture.step03.repo;


import lecture.step03.domain.Category;

import java.util.List;

public interface CategoryRepository {
    long insert(String name);

    List<Category> findAll();

    boolean deleteById(long id);
}
