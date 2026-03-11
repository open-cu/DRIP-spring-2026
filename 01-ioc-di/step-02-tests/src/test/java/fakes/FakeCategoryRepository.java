package fakes;

import lecture.step02.domain.Category;
import lecture.step02.repo.CategoryRepository;
import lecture.step02.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class FakeCategoryRepository implements CategoryRepository {

    public final List<Category> inserted = new ArrayList<>();

    private long seq = 0;

    @Override
    public long insert(String name) {
        long id = ++seq;
        inserted.add(new Category(id, name));
        return id;
    }

    @Override
    public List<Category> findAll() {
        return null;
    }

    @Override
    public boolean deleteById(long id) {
        return false;
    }
}
