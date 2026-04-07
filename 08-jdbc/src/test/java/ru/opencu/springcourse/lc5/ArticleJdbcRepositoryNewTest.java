package ru.opencu.springcourse.lc5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.lc1.repository.ArticleRepository;
import ru.opencu.springcourse.lc3.repository.ArticleJdbcRepository;
import ru.opencu.springcourse.lc3.repository.AuthorJdbcRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * @JdbcTest поднимает только:
 *   — DataSource (H2 in-memory автоматически)
 *   — JdbcTemplate / NamedParameterJdbcTemplate
 *   — schema.sql + data.sql
 *
 * Каждый @Test оборачивается в транзакцию и откатывается → полная изоляция.
 * @SpringBootTest не нужен — сервисный слой не поднимается.
 */
@JdbcTest
@Import({ArticleJdbcRepository.class, AuthorJdbcRepository.class})
class ArticleJdbcRepositoryNewTest {

    @Autowired
    ArticleRepository repository;

    @Autowired
    JdbcTemplate jdbc;

    // -------------------------------------------------------------------------
    // ШАГ 1: базовые тесты save / findById
    // -------------------------------------------------------------------------

    @Test
    void saveShouldAssignGeneratedId() {
        Article saved = repository.save(new Article("New Article", "text", "Nature"));

        assertThat(saved.getId()).isPositive();
    }

    @Test
    void findByIdShouldReturnSavedArticle() {
        Article saved = repository.save(new Article("Clean Code", "text", "Science"));

        Optional<Article> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Clean Code");
        assertThat(found.get().getVenue()).isEqualTo("Science");
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(99999L)).isEmpty();
    }

    // -------------------------------------------------------------------------
    // ШАГ 2: демонстрация отката — запускай несколько раз, COUNT не растёт
    // -------------------------------------------------------------------------

    @Test
    void insertedDataShouldBeVisibleWithinTest() {
        repository.save(new Article("Visible inside tx", "text", "Nature"));

        int count = jdbc.queryForObject("SELECT COUNT(*) FROM article", Integer.class);
        assertThat(count).isEqualTo(3); // data.sql вставляет 2 статьи + 1 выше
    }

    // -------------------------------------------------------------------------
    // ШАГ 3: DuplicateKeyException — трансляция из SQLException через @Repository
    // -------------------------------------------------------------------------

    @Test
    void saveShouldThrowDuplicateKeyExceptionOnDuplicateTitle() {
        repository.save(new Article("Unique Title", "text", "Nature"));

        assertThatThrownBy(() ->
                repository.save(new Article("Unique Title", "other text", "Science")))
                .isInstanceOf(DuplicateKeyException.class);
    }

    // -------------------------------------------------------------------------
    // ШАГ 4: @BeforeTransaction / @AfterTransaction — вне транзакции теста
    // -------------------------------------------------------------------------

    @BeforeTransaction
    void beforeTransaction() {
        System.out.println("[BeforeTransaction] — выполняется ДО открытия транзакции теста");
    }

    @AfterTransaction
    void afterTransaction() {
        System.out.println("[AfterTransaction] — выполняется ПОСЛЕ отката транзакции");
    }

    // -------------------------------------------------------------------------
    // Дополнительные тесты
    // -------------------------------------------------------------------------

    @Test
    void findAllShouldReturnArticlesFromDataSql() {
        List<Article> articles = repository.findAll();

        assertThat(articles).hasSize(2);
        assertThat(articles).extracting(Article::getTitle)
                .containsExactlyInAnyOrder(
                        "Introduction to Spring JDBC",
                        "Design Patterns in Java");
    }

    @Test
    void deleteShouldRemoveArticle() {
        Article saved = repository.save(new Article("To Delete", "text", "Nature"));

        repository.delete(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}
