package ru.opencu.springcourse.lc3.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.opencu.springcourse.domain.ArticleReview;
import ru.opencu.springcourse.lc1.repository.ArticleReviewRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class ArticleReviewJdbcRepository implements ArticleReviewRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public ArticleReviewJdbcRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    @Override
    public Optional<ArticleReview> findById(long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("SELECT * FROM article_review WHERE id = ?", reviewMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ArticleReview> findByArticleId(long articleId) {
        return jdbc.query(
                "SELECT * FROM article_review WHERE article_id = ?",
                reviewMapper, articleId);
    }

    @Override
    public ArticleReview save(ArticleReview review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(
                "INSERT INTO article_review (text, article_id) VALUES (:text, :articleId)",
                new MapSqlParameterSource()
                        .addValue("text",      review.getText())
                        .addValue("articleId", review.getArticleId()),
                keyHolder,
                new String[]{"id"});
        review.setId(keyHolder.getKey().longValue());
        return review;
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE FROM article_review WHERE id = ?", id);
    }

    private final RowMapper<ArticleReview> reviewMapper = (rs, rowNum) -> {
        ArticleReview r = new ArticleReview();
        r.setId(rs.getLong("id"));
        r.setText(rs.getString("text"));
        r.setArticleId(rs.getLong("article_id"));
        return r;
    };
}
