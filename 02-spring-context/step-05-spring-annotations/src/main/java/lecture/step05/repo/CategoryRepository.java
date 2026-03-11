package lecture.step05.repo;


import lecture.step05.domain.Category;

import java.util.List;

public interface CategoryRepository {
    long insert(String name);

    List<Category> findAll();

    boolean deleteById(long id);
}
