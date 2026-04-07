package ru.opencu.springcourse.livecoding4;

import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Шаг 1: @EventListener vs @TransactionalEventListener.
 *
 * Ключевой вопрос: когда должен выполняться слушатель относительно транзакции издателя?
 */
@Component
public class ArticleEventListener {

    private final JdbcTemplate jdbc;

    public ArticleEventListener(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // =========================================================================
    // Вариант 1: @EventListener — внутри транзакции издателя
    // =========================================================================

    /**
     * Вызывается СРАЗУ при publishEvent(), внутри той же транзакции что и издатель.
     *
     * Плюс:  изменения слушателя в одной транзакции с основными данными (атомарно).
     * Минус: исключение в слушателе откатит ВСЮ транзакцию издателя.
     * Минус: не видит результат транзакции — данные ещё не зафиксированы.
     */
    @EventListener
    public void onPublished_inTransaction(ArticlePublishedEvent event) {
        System.out.println("[EventListener] статья " + event.getArticleId()
                + " — выполняется ВНУТРИ транзакции издателя");
        // любые исключения здесь откатят всю транзакцию!
    }

    // =========================================================================
    // Вариант 2: @TransactionalEventListener(BEFORE_COMMIT)
    // =========================================================================

    /**
     * Вызывается непосредственно ПЕРЕД коммитом транзакции издателя.
     * Выполняется в той же транзакции что и издатель.
     *
     * Применяется для: валидации состояния перед фиксацией.
     * Исключение здесь откатит транзакцию.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onPublished_beforeCommit(ArticlePublishedEvent event) {
        System.out.println("[TEL BEFORE_COMMIT] статья " + event.getArticleId()
                + " — валидация перед коммитом");
    }

    // =========================================================================
    // Вариант 3: @TransactionalEventListener(AFTER_COMMIT)
    // =========================================================================

    /**
     * Вызывается ПОСЛЕ успешного коммита транзакции издателя.
     * Данные уже зафиксированы в БД — безопасно для внешних вызовов.
     *
     * Плюс:  данные точно зафиксированы, можно безопасно отправить уведомление,
     *         обновить кэш, вызвать внешний сервис.
     * Минус: не участвует в транзакции издателя (уже закрыта).
     *
     * ВАЖНО: нужен REQUIRES_NEW — после AFTER_COMMIT транзакции издателя уже нет.
     *        Без него метод выполнится без транзакции.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPublished_afterCommit(ArticlePublishedEvent event) {
        System.out.println("[TEL AFTER_COMMIT] статья " + event.getArticleId()
                + " — выполняется ПОСЛЕ коммита, в новой транзакции");
        jdbc.update("UPDATE outbox_event SET processed = TRUE WHERE payload = ?",
                "article_id=" + event.getArticleId());
    }

    // =========================================================================
    // Вариант 4: @TransactionalEventListener(AFTER_ROLLBACK)
    // =========================================================================

    /**
     * Вызывается если транзакция издателя ОТКАТИЛАСЬ.
     * Используется для компенсирующих действий (saga / compensating transaction).
     *
     * ВАЖНО: нельзя делать изменения в БД в той же транзакции (её нет).
     *        Для записи используйте REQUIRES_NEW.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onPublished_afterRollback(ArticlePublishedEvent event) {
        System.out.println("[TEL AFTER_ROLLBACK] откат для статьи " + event.getArticleId()
                + " — компенсирующее действие");
    }

    // =========================================================================
    // Вариант 5: @TransactionalEventListener(AFTER_COMPLETION)
    // =========================================================================

    /**
     * Вызывается ПОСЛЕ завершения транзакции — и после коммита, и после отката.
     * Аналог finally: выполняется всегда.
     *
     * Применяется для: очистки ресурсов, логирования итога транзакции.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void onPublished_afterCompletion(ArticlePublishedEvent event) {
        System.out.println("[TEL AFTER_COMPLETION] транзакция завершена для статьи "
                + event.getArticleId());
    }
}
