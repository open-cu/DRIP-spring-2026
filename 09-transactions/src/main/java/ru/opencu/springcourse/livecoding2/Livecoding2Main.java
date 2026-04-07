package ru.opencu.springcourse.livecoding2;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.opencu.springcourse.Main;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;

/**
 * ЛК#2 — Запуск: декларативные транзакции через @Transactional.
 *
 * Темы:
 *   1. @Transactional на классе — REQUIRED по умолчанию
 *   2. rollbackFor  — откат и на checked-исключения
 *   3. noRollbackFor — исключение НЕ откатывает транзакцию
 *   4. Проблема self-invocation и её решение через отдельный бин
 */
public class Livecoding2Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
        ArticleTransactionalService service = ctx.getBean(ArticleTransactionalService.class);

        // ---- Шаг 1: базовый @Transactional ---------------------------------
        System.out.println("\n=== Шаг 1: базовый @Transactional (REQUIRED) ===");
        Article a1 = service.create(new Article("Declarative TX Article", "content"));
        System.out.println("Создана: " + a1);

        System.out.println("\n=== Шаг 1: readOnly транзакция — все статьи ===");
        service.findAll().forEach(System.out::println);

        // ---- Шаг 2: rollbackFor --------------------------------------------
        System.out.println("\n=== Шаг 2: rollbackFor = Exception.class ===");
        try {
            service.importFromCsv("");  // checked Exception → откат
        } catch (Exception e) {
            System.out.println("Ожидаемое исключение: " + e.getMessage());
        }

        // ---- Шаг 3: noRollbackFor ------------------------------------------
        System.out.println("\n=== Шаг 3: noRollbackFor — лог сохранится, статья нет ===");
        try {
            service.updateWithLogging(a1.getId(), "");  // blank title → IAE, но без отката
        } catch (IllegalArgumentException e) {
            System.out.println("Исключение: " + e.getMessage() + " (outbox_event НЕ откатился)");
        }

        // ---- Шаг 4: self-invocation ----------------------------------------
        System.out.println("\n=== Шаг 4: self-invocation проблема и решение ===");
        service.create(new Article("Draft 1", "d1"));
        service.create(new Article("Draft 2", "d2"));

        System.out.println("BROKEN: this.publishArticle() обходит прокси — @Transactional игнорируется");
        service.publishAllDrafts_BROKEN();

        System.out.println("FIXED:  вызов через другой бин (ArticlePublishingService) — прокси работает");
        service.publishAllDrafts_FIXED();

        System.out.println("\n=== Итого PUBLISHED ===");
        service.findByStatus(ArticleStatus.PUBLISHED).forEach(System.out::println);
    }
}
