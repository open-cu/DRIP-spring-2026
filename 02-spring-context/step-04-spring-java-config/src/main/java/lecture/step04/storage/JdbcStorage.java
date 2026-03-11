package lecture.step04.storage;

import lecture.step04.domain.Category;

import java.util.List;

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