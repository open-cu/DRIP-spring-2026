package lecture.step05.storage;

import lecture.step05.domain.Category;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("inMemoryStorage")
@Profile("demo")
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
