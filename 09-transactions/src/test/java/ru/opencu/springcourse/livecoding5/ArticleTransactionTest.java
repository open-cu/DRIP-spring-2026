package ru.opencu.springcourse.livecoding5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;
import ru.opencu.springcourse.livecoding2.ArticlePublishingService;
import ru.opencu.springcourse.livecoding2.ArticleTransactionalService;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * @SpringBootTest — полный контекст приложения.
 * Используется здесь потому что тестируем поведение @Transactional на сервисе,
 * а не только репозиторий (для которого достаточно @JdbcTest slice).
 *
 * @Transactional на классе теста:
 *   — каждый @Test выполняется в своей транзакции
 *   — транзакция автоматически ОТКАТЫВАЕТСЯ после теста (по умолчанию)
 *   — база возвращается в исходное состояние после каждого теста
 *   — тесты изолированы друг от друга без ручной очистки
 */
@SpringBootTest
@Transactional
class ArticleTransactionTest {

    @Autowired ArticleTransactionalService articleService;
    @Autowired ArticlePublishingService publishingService;
    @Autowired JdbcTemplate jdbc;

    // =========================================================================
    // Шаг 1: базовый тест с автооткатом
    // =========================================================================

    /**
     * @Transactional на тесте → откат после теста.
     * Созданная статья существует в рамках теста, но исчезает после него.
     */
    @Test
    void createShouldSaveArticle() {
        Article created = articleService.create(new Article("Test TX Article", "content"));

        assertThat(created.getId()).isPositive();
        assertThat(articleService.findById(created.getId())).isPresent();
        // После теста — ROLLBACK автоматически
    }

    // =========================================================================
    // Шаг 2: setRollbackOnly — транзакция помечена для отката, но ещё не откатилась
    // =========================================================================

    @Test
    void createShouldRollbackOnDuplicate() {

        // транзакция

        articleService.create(new Article("Unique Article", "content"));

        // транзакция
        assertThatThrownBy(() ->
                articleService.create(new Article("Unique Article", "other content")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(DuplicateKeyException.class);
        // транзакция

        // Транзакция помечена setRollbackOnly, но ещё НЕ откатилась.

        long count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM article WHERE title = 'Unique Article'", Long.class);
        assertThat(count).isEqualTo(1);
        // транзакция


        // транзакция остановлена
        TestTransaction.end();

        // откат з-за дублирования

        count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM article WHERE title = 'Unique Article'", Long.class);
        assertThat(count).isEqualTo(0);
    }

    // =========================================================================
    // Шаг 3: TestTransaction — управление транзакцией теста из кода
    // =========================================================================

    /**
     * TestTransaction позволяет управлять транзакцией теста программно:
     *   TestTransaction.start()             — начать новую транзакцию
     *   TestTransaction.end()               — завершить транзакцию (коммит или откат)
     *   TestTransaction.flagForCommit()     — пометить для коммита
     *   TestTransaction.flagForRollback()   — пометить для отката (default)
     *   TestTransaction.isActive()          — активна ли транзакция
     *   TestTransaction.isFlaggedForRollback() — помечена ли для отката
     *
     * Полезно для тестов которым нужно несколько транзакций, например:
     * проверка что REQUIRES_NEW сохраняет данные при откате внешней.
     */
    @Test
    @Rollback(false)    // снимаем дефолтный откат — управляем сами через TestTransaction
    void demonstrateTestTransaction() {
        // Шаг 1: создаём статью в первой транзакции
        Article article = articleService.create(new Article("TestTransaction Demo", "content"));
        assertThat(TestTransaction.isActive()).isTrue();
        assertThat(TestTransaction.isFlaggedForRollback()).isFalse();   // @Rollback(false)

        // Коммитим и заканчиваем первую транзакцию
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // Шаг 2: начинаем новую транзакцию и откатываем её
        TestTransaction.start();
        articleService.create(new Article("Will Be Rolled Back", "content"));

        TestTransaction.flagForRollback();
        TestTransaction.end();

        // Шаг 3: проверяем в чистой транзакции — должна быть только первая статья
        TestTransaction.start();
        assertThat(articleService.findById(article.getId())).isPresent();
        long count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM article WHERE title = 'Will Be Rolled Back'", Long.class);
        assertThat(count).isZero();

        // Очищаем за собой
        jdbc.update("DELETE FROM article WHERE title = 'TestTransaction Demo'");
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    // =========================================================================
    // Шаг 4: @BeforeTransaction / @AfterTransaction
    // =========================================================================

    /**
     * @BeforeTransaction — вызывается ДО открытия транзакции теста.
     * @AfterTransaction  — вызывается ПОСЛЕ коммита/отката транзакции теста.
     *
     * Выполняются вне транзакции. Полезны для:
     *   — подготовки данных которые должны быть видны до транзакции
     *   — проверки состояния БД после отката
     */
    @BeforeTransaction
    void beforeTx() {
        System.out.println("[BeforeTransaction] — перед открытием транзакции теста");
    }

    @AfterTransaction
    void afterTx() {
        System.out.println("[AfterTransaction] — после отката/коммита транзакции теста");
    }
}
