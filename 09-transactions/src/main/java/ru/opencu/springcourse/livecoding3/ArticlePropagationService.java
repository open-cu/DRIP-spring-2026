package ru.opencu.springcourse.livecoding3;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;
import ru.opencu.springcourse.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Propagation описывает поведение метода при наличии уже открытой транзакции:
 *
 *   REQUIRED        — присоединиться к существующей, или открыть новую (default)
 *   REQUIRES_NEW    — всегда открывать новую, приостанавливая текущую
 *   SUPPORTS        — использовать существующую, или выполниться без транзакции
 *   NOT_SUPPORTED   — всегда выполняться без транзакции, приостанавливая текущую
 *   MANDATORY       — присоединиться к существующей; без неё — IllegalTransactionStateException
 *   NEVER           — выполняться без транзакции; при наличии — IllegalTransactionStateException
 *   NESTED          — вложенная транзакция с savepoint (только DataSourceTransactionManager)
 */
@Service
public class ArticlePropagationService {

    private final ArticleRepository articleRepository;
    private final JdbcTemplate jdbc;

    public ArticlePropagationService(ArticleRepository articleRepository,
                                     JdbcTemplate jdbc) {
        this.articleRepository = articleRepository;
        this.jdbc = jdbc;
    }

    // =========================================================================
    // Шаг 1: REQUIRED (default)
    // =========================================================================

    /**
     * REQUIRED: присоединяется к существующей транзакции или открывает новую.
     * Откат во вложенном методе → откат всей внешней транзакции.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Article create(Article article) {
        Article saved = articleRepository.save(article);
        writeAuditLog(saved.getId(), "CREATED");    // участвует в той же транзакции
        return saved;
    }

    // =========================================================================
    // Шаг 2: REQUIRES_NEW
    // =========================================================================

    /**
     * REQUIRES_NEW: всегда открывает новую независимую транзакцию.
     * Текущая транзакция приостанавливается на время выполнения.
     *
     * Применяется для:
     *   — аудит-лога (должен сохраниться даже при откате основной транзакции)
     *   — outbox-записей
     *   — операций, которые не должны откатываться вместе с вызывающим кодом
     *
     * ВАЖНО: вызывать только через другой бин или внедрить себя (self-invocation!)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void writeAuditLog(long articleId, String action) {
        jdbc.update("INSERT INTO outbox_event (event_type, payload) VALUES (?, ?)",
                action, "article_id=" + articleId);
    }

    // =========================================================================
    // Шаг 3: SUPPORTS, NOT_SUPPORTED, NEVER, MANDATORY
    // =========================================================================

    /**
     * SUPPORTS: использует текущую транзакцию если есть, иначе без транзакции.
     * Подходит для методов, которые одинаково работают в обоих режимах.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    /**
     * NOT_SUPPORTED: приостанавливает текущую транзакцию, выполняется без неё.
     * Применяется когда операция не должна участвовать в транзакции
     * (например, вызов внешнего сервиса, который не поддерживает откат).
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public long countAll() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM article", Long.class);
    }

    /**
     * MANDATORY: требует существующей транзакции; без неё — IllegalTransactionStateException.
     * Применяется для методов, которые должны вызываться только внутри транзакции.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateStatus(long id, ArticleStatus status) {
        articleRepository.updateStatus(id, status);
    }

    /**
     * NEVER: требует отсутствия транзакции; при наличии — IllegalTransactionStateException.
     * Применяется для операций, которые явно не должны выполняться в транзакции.
     */
    @Transactional(propagation = Propagation.NEVER)
    public List<Article> findAllNonTransactional() {
        return articleRepository.findAll();
    }

    /**
     * NESTED: вложенная транзакция с savepoint.
     *   — при откате вложенной: откат только до savepoint, внешняя продолжается
     *   — при откате внешней: откатываются все вложенные
     *
     * Поддерживается только DataSourceTransactionManager (не JPA/JTA).
     * Используется для частичного отката в цикле обработки.
     */
    @Transactional(propagation = Propagation.NESTED)
    public Article tryCreateNested(Article article) {
        return articleRepository.save(article);
    }

    // =========================================================================
    // Шаг 4: readOnly
    // =========================================================================

    /**
     * readOnly = true — подсказка провайдеру транзакций:
     *   — JDBC: может оптимизировать соединение
     *   — JPA: отключает автоматический dirty-check и flush
     *   — Некоторые datasource-proxy маршрутизируют read-only транзакции на реплику
     *
     * НЕ запрещает запись на уровне SQL — это только подсказка.
     */
    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Article> findByStatus(ArticleStatus status) {
        return articleRepository.findByStatus(status);
    }

    // =========================================================================
    // Шаг 5: timeout
    // =========================================================================

    /**
     * timeout = 5 — откат транзакции если метод выполняется дольше 5 секунд.
     * Бросает TransactionTimedOutException.
     *
     * Полезно для защиты от медленных запросов в production.
     */
    @Transactional(timeout = 5)
    public List<Article> findAllWithTimeout() {
        return articleRepository.findAll();
    }

    // =========================================================================
    // Шаг 6: isolation
    // =========================================================================

    /**
     * Явный уровень изоляции — переопределяет дефолт datasource.
     *
     * READ_UNCOMMITTED — грязное чтение (видны незафиксированные изменения)
     * READ_COMMITTED   — нет грязного чтения (PostgreSQL default, рекомендован)
     * REPEATABLE_READ  — нет неповторяемого чтения (MySQL InnoDB default)
     * SERIALIZABLE     — полная изоляция, максимальные блокировки
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Article createWithExplicitIsolation(Article article) {
        return articleRepository.save(article);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public List<Article> findAllRepeatableRead() {
        return articleRepository.findAll();
    }
}
