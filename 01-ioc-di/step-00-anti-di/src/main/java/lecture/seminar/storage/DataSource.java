
package lecture.seminar.storage;


import lecture.seminar.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private final List<Category> table = new ArrayList<>();

    public void insert(Category category) {
        table.add(category);
        System.out.println("DB: inserted " + category);
    }

    public List<Category> selectAll() {
        System.out.println("DB: select * from categories");
        return List.copyOf(table);
    }

    public boolean deleteById(long id) {
        System.out.println("DB: delete from categories where id=" + id);
        return table.removeIf(c -> c.id() == id);
    }
}