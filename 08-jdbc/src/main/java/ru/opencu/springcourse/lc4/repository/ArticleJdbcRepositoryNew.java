package ru.opencu.springcourse.lc4.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.lc1.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class ArticleJdbcRepositoryNew implements ArticleRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public ArticleJdbcRepositoryNew(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    @Override
    public Optional<Article> findById(long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(
                    "SELECT a.id, a.title, a.text, v.name AS venue " +
                            "FROM article a JOIN venue v ON v.id = a.venue_id WHERE a.id = ?",
                    articleMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Article> findAll() {
        return jdbc.query(
                "SELECT a.id, a.title, a.text, v.name AS venue " +
                        "FROM article a JOIN venue v ON v.id = a.venue_id",
                articleMapper);
    }

    @Override
    public Article save(Article article) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(
                "INSERT INTO article (title, text, venue_id) " +
                        "VALUES (:title, :text, (SELECT id FROM venue WHERE name = :venue))",
                new MapSqlParameterSource()
                        .addValue("title", article.getTitle())
                        .addValue("text", article.getText())
                        .addValue("venue", article.getVenue()),
                keyHolder,
                new String[]{"id"});
        article.setId(keyHolder.getKey().longValue());
        return article;
    }

    @Override
    public void update(Article article) {
        namedJdbc.update(
                "UPDATE article SET title = :title, text = :text, " +
                        "venue_id = (SELECT id FROM venue WHERE name = :venue) WHERE id = :id",
                new MapSqlParameterSource()
                        .addValue("title", article.getTitle())
                        .addValue("text", article.getText())
                        .addValue("venue", article.getVenue())
                        .addValue("id", article.getId()));
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE FROM article WHERE id = ?", id);
    }

    @Override
    public List<Article> findAllWithAuthors() {
        throw new UnsupportedOperationException("не используется в ЛК#4");
    }

    private final RowMapper<Article> articleMapper = (rs, rowNum) -> {
        Article a = new Article();
        a.setId(rs.getLong("id"));
        a.setTitle(rs.getString("title"));
        a.setText(rs.getString("text"));
        a.setVenue(rs.getString("venue"));
        return a;
    };
}
