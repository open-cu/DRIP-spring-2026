
package lecture.seminar.repo;


import lecture.seminar.domain.Category;
import lecture.seminar.storage.JdbcStorage;
import lecture.seminar.storage.Storage;

import java.util.List;

public class CategoryRepository {

    private final Storage storage = new JdbcStorage(); // anti-DI
    private long seq = 0;

    public long insert(String name) {
        long id = ++seq;
        storage.insert(new Category(id, name));
        return id;
    }
    public List<Category> findAll() {
        return storage.findAll();
    }

    public boolean deleteById(long id) {
        return storage.deleteById(id);
    }
}
