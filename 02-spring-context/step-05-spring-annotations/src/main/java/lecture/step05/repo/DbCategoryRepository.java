package lecture.step05.repo;

import lecture.step05.domain.Category;
import lecture.step05.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
