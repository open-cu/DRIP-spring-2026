package ru.opencu.springcourse.lc3.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Один запрос с WHERE article_id IN (:ids) вместо N запросов по одному.
 * Вызывается из ArticleService.findAllWithAuthors_inQuery().
 */
@Repository
public class AuthorJdbcRepository {

    private final NamedParameterJdbcTemplate namedJdbc;

    public AuthorJdbcRepository(NamedParameterJdbcTemplate namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    /** Возвращает Map: article_id → список имён авторов. Один запрос на любое число статей. */
    public Map<Long, List<String>> findAuthorNamesByArticleIds(List<Long> articleIds) {
        if (articleIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String sql = """
                SELECT aa.article_id, au.name
                FROM article_author aa
                JOIN author au ON au.id = aa.author_id
                WHERE aa.article_id IN (:ids)
                """;

        Map<Long, List<String>> result = new HashMap<>();
        namedJdbc.query(sql, new MapSqlParameterSource("ids", articleIds), rs -> {
            result.computeIfAbsent(rs.getLong("article_id"), k -> new ArrayList<>())
                  .add(rs.getString("name"));
        });
        return result;
    }
}
