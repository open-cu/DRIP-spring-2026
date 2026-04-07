package ru.opencu.springcourse.livecoding1;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.opencu.springcourse.Main;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;

/**
 * ЛК#1 — Запуск: программное управление транзакциями.
 *
 * Темы:
 *   1. PlatformTransactionManager: begin / commit / rollback вручную
 *   2. TransactionTemplate.execute()          — возвращает результат
 *   3. TransactionTemplate.executeWithoutResult() — void
 *   4. status.setRollbackOnly()               — откат без исключения
 *   5. readOnly TransactionTemplate           — отдельный экземпляр
 */
public class Livecoding1Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
        ArticleProgrammaticTxService service = ctx.getBean(ArticleProgrammaticTxService.class);

        // ---- Шаг 1: PlatformTransactionManager напрямую --------------------
        System.out.println("\n=== Шаг 1: PlatformTransactionManager (begin/commit/rollback) ===");
        Article a1 = service.createWithPtm(new Article("PTM Article", "created via PTM"));
        System.out.println("Создана: " + a1);

        // ---- Шаг 2а: TransactionTemplate.execute() -------------------------
        System.out.println("\n=== Шаг 2а: TransactionTemplate.execute() ===");
        Article a2 = service.createWithTemplate(new Article("Template Article", "created via template"));
        System.out.println("Создана: " + a2);

        // ---- Шаг 2б: setRollbackOnly() — откат без исключения --------------
        System.out.println("\n=== Шаг 2б: setRollbackOnly() — статья 'forbidden' не сохранится ===");
        Article rolledBack = service.createWithTemplateAndRollback(new Article("forbidden title", "x"));
        System.out.println("Результат: " + rolledBack);   // null — транзакция откатилась

        Article saved = service.createWithTemplateAndRollback(new Article("Allowed Title", "y"));
        System.out.println("Результат: " + saved);        // сохранилась

        // ---- Шаг 2в: executeWithoutResult() --------------------------------
        System.out.println("\n=== Шаг 2в: executeWithoutResult() — архивируем статью ===");
        service.archiveArticle(a2.getId());
        System.out.println("Статья " + a2.getId() + " переведена в ARCHIVED");

        // ---- Шаг 2г: readOnly TransactionTemplate --------------------------
        System.out.println("\n=== Шаг 2г: readOnly TransactionTemplate ===");
        long count = service.countPublished();
        System.out.println("Опубликовано статей: " + count);
    }
}
