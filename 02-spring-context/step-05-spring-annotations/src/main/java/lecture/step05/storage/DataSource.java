package lecture.step05.storage;

import lecture.step05.domain.Category;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Profile("prod")
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
    }}
