package lecture.step02.repo;



import lecture.step02.domain.Category;

import java.util.List;

public interface CategoryRepository {
    long insert(String name);

    List<Category> findAll();

    boolean deleteById(long id);
}
