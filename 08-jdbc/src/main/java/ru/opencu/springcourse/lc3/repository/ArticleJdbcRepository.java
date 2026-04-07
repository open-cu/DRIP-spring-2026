package ru.opencu.springcourse.lc3.repository;

import org.springframework.context.annotation.Primary;
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

import java.util.*;

/**
 *
 * Что исчезло:
 *   — try/finally/close — JdbcTemplate управляет ресурсами сам
 *   — checked SQLException — JdbcTemplate транслирует в DataAccessException
 *   — ручной маппинг размазан по методам — выделен в один RowMapper
 */
@Repository
@Primary
public class ArticleJdbcRepository implements ArticleRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public ArticleJdbcRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // -------------------------------------------------------------------------
    // Базовый CRUD
    // -------------------------------------------------------------------------

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
                        .addValue("text",  article.getText())
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
                        .addValue("text",  article.getText())
                        .addValue("venue", article.getVenue())
                        .addValue("id",    article.getId()));
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE FROM article WHERE id = ?", id);
    }

    // -------------------------------------------------------------------------
    // N+1: ВАРИАНТ 1 — наивный (показываем проблему в логах)
    // -------------------------------------------------------------------------

    public List<Article> findAllWithAuthors_naive() {
        List<Article> articles = findAll();                     // запрос 1
        for (Article article : articles) {
            List<String> authors = jdbc.queryForList(           // запрос 2..N+1
                    "SELECT au.name FROM author au " +
                    "JOIN article_author aa ON aa.author_id = au.id " +
                    "WHERE aa.article_id = ?",
                    String.class, article.getId());
            article.setAuthors(authors);
        }
        return articles;
    }

    // -------------------------------------------------------------------------
    // N+1: ВАРИАНТ 2 — один JOIN + ResultSetExtractor
    // -------------------------------------------------------------------------

    @Override
    public List<Article> findAllWithAuthors() {
        String sql = """
                SELECT a.id, a.title, a.text, v.name AS venue, au.name AS author_name
                FROM article a
                JOIN venue v ON v.id = a.venue_id
                LEFT JOIN article_author aa ON aa.article_id = a.id
                LEFT JOIN author au ON au.id = aa.author_id
                ORDER BY a.id
                """;
        return jdbc.query(sql, rs -> {
            Map<Long, Article> result = new LinkedHashMap<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                Article a = result.computeIfAbsent(id, k -> {
                    Article article = new Article();
                    try {
                        article.setId(rs.getLong("id"));
                        article.setTitle(rs.getString("title"));
                        article.setText(rs.getString("text"));
                        article.setVenue(rs.getString("venue"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return article;
                });
                String authorName = rs.getString("author_name");
                if (authorName != null) {
                    a.getAuthors().add(authorName);
                }
            }
            return new ArrayList<>(result.values());
        });
    }

    // -------------------------------------------------------------------------
    // RowMapper — выделен в одно место
    // -------------------------------------------------------------------------

    private final RowMapper<Article> articleMapper = (rs, rowNum) -> {
        Article a = new Article();
        a.setId(rs.getLong("id"));
        a.setTitle(rs.getString("title"));
        a.setText(rs.getString("text"));
        a.setVenue(rs.getString("venue"));
        return a;
    };
}
