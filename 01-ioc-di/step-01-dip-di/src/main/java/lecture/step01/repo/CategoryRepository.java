package lecture.step01.repo;


import lecture.step01.domain.Category;

import java.util.List;

public interface CategoryRepository {
    long insert(String name);

    List<Category> findAll();

    boolean deleteById(long id);
}
