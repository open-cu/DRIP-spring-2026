package ru.opencu.springcourse.livecoding4;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.opencu.springcourse.Main;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.livecoding2.ArticleTransactionalService;

/**
 * ЛК#4 — Запуск: TransactionalEventListener и TransactionSynchronization.
 *
 * Темы:
 *   1. @EventListener — внутри транзакции издателя
 *   2. @TransactionalEventListener(BEFORE_COMMIT)   — перед коммитом
 *   3. @TransactionalEventListener(AFTER_COMMIT)    — после коммита, REQUIRES_NEW
 *   4. @TransactionalEventListener(AFTER_ROLLBACK)  — компенсация при откате
 *   5. @TransactionalEventListener(AFTER_COMPLETION)— всегда (аналог finally)
 *   6. TransactionSynchronization.registerSynchronization() — хуки без событий
 *   7. TransactionSynchronizationManager — состояние текущей транзакции
 *   8. bindResource / unbindResource — привязка ресурсов к транзакции
 */
public class Livecoding4Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);

        ArticleTransactionalService articleService = ctx.getBean(ArticleTransactionalService.class);
        ArticleEventPublishingService eventService = ctx.getBean(ArticleEventPublishingService.class);
        ArticleSyncDemoService syncService = ctx.getBean(ArticleSyncDemoService.class);

        // ---- Шаг 1-5: TransactionalEventListener ---------------------------
        System.out.println("\n=== Шаг 1-5: публикуем статью → наблюдаем фазы TEL ===");
        System.out.println("--- Ожидаемый порядок вызовов:");
        System.out.println("    1. @EventListener          — сразу при publishEvent()");
        System.out.println("    2. @TEL(BEFORE_COMMIT)     — перед коммитом (в транзакции)");
        System.out.println("    3. коммит транзакции");
        System.out.println("    4. @TEL(AFTER_COMMIT)      — после коммита (REQUIRES_NEW)");
        System.out.println("    5. @TEL(AFTER_COMPLETION)  — после любого завершения");
        System.out.println();

        Article draft = articleService.create(new Article("Event Demo Article", "content"));
        eventService.publishArticle(draft.getId());

        // ---- Шаг 6-7: TransactionSynchronization ---------------------------
        System.out.println("\n=== Шаг 6: TransactionSynchronization.registerSynchronization() ===");
        System.out.println("--- Ожидаемый порядок колбэков:");
        System.out.println("    1. beforeCommit()");
        System.out.println("    2. afterCommit()");
        System.out.println("    3. afterCompletion(COMMITTED)");
        System.out.println();

        syncService.createWithSynchronization(new Article("Sync Demo Article", "content"));

        // ---- Шаг 7: TransactionSynchronizationManager ----------------------
        System.out.println("\n=== Шаг 7: TransactionSynchronizationManager — состояние транзакции ===");
        syncService.demonstrateTxState();

        // ---- Шаг 8: bindResource -------------------------------------------
        System.out.println("\n=== Шаг 8: bindResource / unbindResource ===");
        syncService.createWithResourceBinding(new Article("Resource Binding Article", "content"));
        System.out.println("Ресурс освобождён в afterCompletion");
    }
}
