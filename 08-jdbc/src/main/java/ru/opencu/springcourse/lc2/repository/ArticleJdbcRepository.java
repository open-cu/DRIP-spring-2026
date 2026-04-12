package ru.opencu.springcourse.lc2.repository;

import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.lc1.repository.ArticleRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * Три проблемы:
 *   1. Checked SQLException — обязателен к обработке, но сделать с ним нечего.
 *   2. Одинаковый try/finally/close-boilerplate в каждом методе.
 *   3. Ручной маппинг ResultSet → объект, хрупкий при переименовании столбцов.
 *
 * Намеренно без @Repository — Spring не подхватывает этот класс.
 */
public class ArticleJdbcRepository implements ArticleRepository {

    private final DataSource dataSource;

    public ArticleJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // --- ПРОБЛЕМА 2: весь этот try/finally повторяется в каждом методе ------

    @Override
    public Optional<Article> findById(long id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(
                    "SELECT a.id, a.title, a.text, v.name AS venue " +
                    "FROM article a JOIN venue v ON v.id = a.venue_id WHERE a.id = ?");
            ps.setLong(1, id);
            rs = ps.executeQuery();
            return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);          // ПРОБЛЕМА 1: что с ним делать?
        } finally {
            closeAll(rs, ps, conn);                 // ПРОБЛЕМА 2: не забыть закрыть всё
        }
    }

    @Override
    public List<Article> findAll() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(
                    "SELECT a.id, a.title, a.text, v.name AS venue " +
                    "FROM article a JOIN venue v ON v.id = a.venue_id");
            rs = ps.executeQuery();
            List<Article> result = new ArrayList<>();
            while (rs.next()) result.add(mapRow(rs));
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeAll(rs, ps, conn);
        }
    }

    @Override
    public Article save(Article article) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet keys = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(
                    "INSERT INTO article (title, text, venue_id) " +
                    "VALUES (?, ?, (SELECT id FROM venue WHERE name = ?))",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, article.getTitle());
            ps.setString(2, article.getText());
            ps.setString(3, article.getVenue());
            ps.executeUpdate();
            keys = ps.getGeneratedKeys();
            if (keys.next()) {
                article.setId(keys.getLong(1));
            }
            return article;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeAll(keys, ps, conn);
        }
    }

    @Override
    public void update(Article article) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(
                    "UPDATE article SET title = ?, text = ?, " +
                    "venue_id = (SELECT id FROM venue WHERE name = ?) WHERE id = ?");
            ps.setString(1, article.getTitle());
            ps.setString(2, article.getText());
            ps.setString(3, article.getVenue());
            ps.setLong(4, article.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeAll(ps, conn);
        }
    }

    @Override
    public void delete(long id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("DELETE FROM article WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeAll(ps, conn);
        }
    }

    @Override
    public List<Article> findAllWithAuthors() {
        throw new UnsupportedOperationException("не реализовано в ЛК#2");
    }

    // ПРОБЛЕМА 3: маппинг вручную — при переименовании столбца ломается в рантайме
    private Article mapRow(ResultSet rs) throws SQLException {
        Article a = new Article();
        a.setId(rs.getLong("id"));
        a.setTitle(rs.getString("title"));
        a.setText(rs.getString("text"));
        a.setVenue(rs.getString("venue"));
        return a;
    }

    private void closeAll(AutoCloseable... resources) {
        for (AutoCloseable r : resources)
            if (r != null) try { r.close(); } catch (Exception ignored) {}
    }
}
