package lecture.step05.storage;

import lecture.step05.domain.Category;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("jdbcStorage")
@Profile("prod")
public class JdbcStorage implements Storage {

    private final DataSource dataSource;

    public JdbcStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Category category) {
        dataSource.insert(category);
    }

    @Override
    public List<Category> findAll() {
        return dataSource.selectAll();
    }

    @Override
    public boolean deleteById(long id) {
        return dataSource.deleteById(id);
    }
}
