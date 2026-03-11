package lecture.step01.storage;


import lecture.step01.domain.Category;

import java.util.Collections;
import java.util.List;

public class DataSource {
    public void insert(Category category) {
        System.out.println("DB: inserted " + category);
    }

    public List<Category> selectAll() {
        System.out.println("DB: selected All " );
        return Collections.emptyList();
    }

    public boolean deleteById(long id) {
        System.out.println("DB: deleted " + id);
        return true;
    }
}
