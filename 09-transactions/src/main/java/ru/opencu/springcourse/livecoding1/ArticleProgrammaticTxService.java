package ru.opencu.springcourse.livecoding1;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;
import ru.opencu.springcourse.repository.ArticleRepository;

/**
 *
 * Два подхода:
 *   1. PlatformTransactionManager напрямую — максимальный контроль, максимум boilerplate
 *   2. TransactionTemplate — убирает boilerplate, аналог JdbcTemplate для транзакций
 *
 * Когда нужен программный подход:
 *   — границы транзакции определяются в рантайме
 *   — нужна транзакция внутри лямбды / колбэка
 *   — самоинвокация вынуждает обойти прокси вручную
 *   — fine-grained управление (частичный коммит внутри цикла)
 */
@Service
public class ArticleProgrammaticTxService {

    private final ArticleRepository articleRepository;
    private final JdbcTemplate jdbc;

    // ---- Подход 1: PlatformTransactionManager напрямую ----------------------
    private final PlatformTransactionManager txManager;

    // ---- Подход 2: TransactionTemplate (предпочтительнее) -------------------
    private final TransactionTemplate txTemplate;
    private final TransactionTemplate readOnlyTxTemplate;

    public ArticleProgrammaticTxService(ArticleRepository articleRepository,
                                        JdbcTemplate jdbc,
                                        PlatformTransactionManager txManager) {
        this.articleRepository = articleRepository;
        this.jdbc = jdbc;
        this.txManager = txManager;

        this.txTemplate = new TransactionTemplate(txManager);

        // readOnly-шаблон — отдельный экземпляр с нужными настройками
        TransactionTemplate roTemplate = new TransactionTemplate(txManager);
        roTemplate.setReadOnly(true);
        this.readOnlyTxTemplate = roTemplate;
    }

    // =========================================================================
    // Подход 1: PlatformTransactionManager напрямую
    // =========================================================================

    /**
     * Шаг 1а: начало, коммит, откат — вручную.
     *
     * DefaultTransactionDefinition — описывает желаемые параметры транзакции:
     *   setPropagationBehavior — поведение при наличии текущей транзакции
     *   setIsolationLevel      — уровень изоляции
     *   setTimeout             — таймаут в секундах (-1 = без ограничений)
     *   setReadOnly            — подсказка провайдеру (отключает flush, dirty-check)
     */
    public Article createWithPtm(Article article) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("createArticle");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        TransactionStatus status = txManager.getTransaction(def);
        // transaction create


        try {
            Article saved = articleRepository.save(article);
            jdbc.update("INSERT INTO outbox_event (event_type, payload) VALUES (?, ?)",
                    "ARTICLE_CREATED", "id=" + saved.getId());

            // transaction commit

            txManager.commit(status);
            return saved;
        } catch (Exception e) {

            // transaction rollback

            txManager.rollback(status);
            throw e;
        }
    }

    // =========================================================================
    // Подход 2: TransactionTemplate
    // =========================================================================

    /**
     * Шаг 2а: execute() — возвращает результат, автоматический откат на RuntimeException.
     */
    public Article createWithTemplate(Article article) {
        return txTemplate.execute(status -> {
            Article saved = articleRepository.save(article);
            jdbc.update("INSERT INTO outbox_event (event_type, payload) VALUES (?, ?)",
                    "ARTICLE_CREATED", "id=" + saved.getId());
            return saved;
            // rollback автоматически при любом RuntimeException
        });
    }

    /**
     * Шаг 2б: setRollbackOnly() — программный откат без броска исключения.
     */
    public Article createWithTemplateAndRollback(Article article) {
        return txTemplate.execute(status -> {
            Article saved = articleRepository.save(article);

            if (saved.getTitle().contains("forbidden")) {
                status.setRollbackOnly();   // транзакция будет откатана при выходе из лямбды
                return null;
            }
            return saved;
        });
    }

    /**
     * Шаг 2в: executeWithoutResult() — void-вариант, без возвращаемого значения.
     */
    public void archiveArticle(long id) {
        txTemplate.executeWithoutResult(status ->
                articleRepository.updateStatus(id, ArticleStatus.ARCHIVED));
    }

    /**
     * Шаг 2г: readOnly-шаблон — отдельный TransactionTemplate с setReadOnly(true).
     */
    public long countPublished() {
        return readOnlyTxTemplate.execute(status ->
                jdbc.queryForObject(
                        "SELECT COUNT(*) FROM article WHERE status = ?",
                        Long.class, ArticleStatus.PUBLISHED.name()));
    }
}
