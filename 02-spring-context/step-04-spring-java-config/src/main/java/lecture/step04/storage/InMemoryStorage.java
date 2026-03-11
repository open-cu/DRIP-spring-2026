package lecture.step04.storage;

import lecture.step04.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorage implements Storage {

    private final List<Category> items = new ArrayList<>();

    @Override
    public void insert(Category category) {
        items.add(category);
        System.out.println("MEM: inserted " + category);
    }

    @Override
    public List<Category> findAll() {
        return items;
    }

    @Override
    public boolean deleteById(long id) {
        return items.removeIf(c -> c.id() == id);
    }
}
