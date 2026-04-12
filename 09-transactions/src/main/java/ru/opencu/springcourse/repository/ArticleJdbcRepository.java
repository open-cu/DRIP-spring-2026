package ru.opencu.springcourse.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;

import java.util.List;
import java.util.Optional;

@Repository
public class ArticleJdbcRepository implements ArticleRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public ArticleJdbcRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    @Override
    public Optional<Article> findById(long id) {
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT * FROM article WHERE id = ?", articleMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Article> findAll() {
        return jdbc.query("SELECT * FROM article", articleMapper);
    }

    @Override
    public List<Article> findByStatus(ArticleStatus status) {
        return jdbc.query("SELECT * FROM article WHERE status = ?", articleMapper, status.name());
    }

    @Override
    public Article save(Article article) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(
                "INSERT INTO article (title, text, status) VALUES (:title, :text, :status)",
                new MapSqlParameterSource()
                        .addValue("title",  article.getTitle())
                        .addValue("text",   article.getText())
                        .addValue("status", article.getStatus().name()),
                keyHolder, new String[]{"id"});
        article.setId(keyHolder.getKey().longValue());
        return article;
    }

    @Override
    public void updateStatus(long id, ArticleStatus status) {
        jdbc.update("UPDATE article SET status = ? WHERE id = ?", status.name(), id);
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE FROM article WHERE id = ?", id);
    }

    private final RowMapper<Article> articleMapper = (rs, rowNum) -> {
        Article a = new Article();
        a.setId(rs.getLong("id"));
        a.setTitle(rs.getString("title"));
        a.setText(rs.getString("text"));
        a.setStatus(ArticleStatus.valueOf(rs.getString("status")));
        return a;
    };
}
