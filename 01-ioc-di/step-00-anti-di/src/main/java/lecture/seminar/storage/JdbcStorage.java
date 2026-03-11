
package lecture.seminar.storage;


import lecture.seminar.domain.Category;

import java.util.List;


public class JdbcStorage implements Storage {

    private final DataSource dataSource = new DataSource(); // anti-DI

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