package lecture.step04.repo;


import lecture.step04.domain.Category;
import lecture.step04.storage.Storage;

import java.util.List;

public class DbCategoryRepository implements CategoryRepository {
    private final Storage storage;
    private long seq = 0;

    public DbCategoryRepository(Storage storage) {
        this.storage = storage;
    }

    @Override
    public long insert(String name) {
        long id = ++seq;
        storage.insert(new Category(id, name));
        return id;
    }

    @Override
    public List<Category> findAll() {
        return storage.findAll();
    }

    @Override
    public boolean deleteById(long id) {
        return storage.deleteById(id);
    }
}
