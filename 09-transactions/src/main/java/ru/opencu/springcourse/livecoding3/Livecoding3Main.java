package ru.opencu.springcourse.livecoding3;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.IllegalTransactionStateException;
import ru.opencu.springcourse.Main;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;

/**
 * ЛК#3 — Запуск: Propagation, ReadOnly, Timeout.
 *
 * Темы:
 *   1. REQUIRED, REQUIRES_NEW — стандартная пара для основной операции + аудита
 *   2. SUPPORTS, NOT_SUPPORTED — опциональное/принудительное отсутствие транзакции
 *   3. MANDATORY — требует существующей транзакции
 *   4. NEVER — запрещает транзакцию
 *   5. NESTED — вложенная транзакция с savepoint
 *   6. readOnly = true — подсказка провайдеру
 *   7. timeout — откат при превышении времени
 *   8. isolation — уровни изоляции
 */
public class Livecoding3Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
        ArticlePropagationService service = ctx.getBean(ArticlePropagationService.class);

        // ---- Шаг 1: REQUIRED + REQUIRES_NEW --------------------------------
        System.out.println("\n=== Шаг 1: REQUIRED + REQUIRES_NEW (аудит-лог) ===");
        // create() вызывает writeAuditLog() с REQUIRES_NEW — аудит в отдельной транзакции
        Article a1 = service.create(new Article("Propagation Article 1", "content"));
        System.out.println("Создана: " + a1);

        // ---- Шаг 2: SUPPORTS -----------------------------------------------
        System.out.println("\n=== Шаг 2: SUPPORTS — работает с транзакцией и без ===");
        // Вызываем вне транзакции — выполнится без неё
        service.findById(a1.getId()).ifPresent(a -> System.out.println("Найдена: " + a));

        // ---- Шаг 3: NOT_SUPPORTED ------------------------------------------
        System.out.println("\n=== Шаг 3: NOT_SUPPORTED — приостанавливает текущую транзакцию ===");
        long total = service.countAll();
        System.out.println("Всего статей (без транзакции): " + total);

        // ---- Шаг 4: MANDATORY — без транзакции бросает исключение ----------
        System.out.println("\n=== Шаг 4: MANDATORY — требует активной транзакции ===");
        try {
            service.updateStatus(a1.getId(), ArticleStatus.PUBLISHED);  // нет транзакции → исключение
        } catch (IllegalTransactionStateException e) {
            System.out.println("Ожидаемое исключение: " + e.getMessage());
        }

        // ---- Шаг 5: NEVER --------------------------------------------------
        System.out.println("\n=== Шаг 5: NEVER — запрещает транзакцию ===");
        // Вызываем без транзакции — ок
        service.findAllNonTransactional().forEach(a -> System.out.println("  " + a));

        // ---- Шаг 6: NESTED -------------------------------------------------
        System.out.println("\n=== Шаг 6: NESTED — savepoint внутри внешней транзакции ===");
        try {
            service.tryCreateNested(new Article("Nested Article", "content"));
            System.out.println("Создана");
        } catch (Exception e) {
            System.out.println("NESTED не поддерживается провайдером: " + e.getMessage());
        }

        // ---- Шаг 7: readOnly -----------------------------------------------
        System.out.println("\n=== Шаг 7: readOnly = true ===");
        service.findAll().forEach(a -> System.out.println("  " + a));
        service.findByStatus(ArticleStatus.DRAFT).forEach(a -> System.out.println("  DRAFT: " + a));

        // ---- Шаг 8: timeout ------------------------------------------------
        System.out.println("\n=== Шаг 8: timeout = 5 сек (TransactionTimedOutException при превышении) ===");
        service.findAllWithTimeout().forEach(a -> System.out.println("  " + a));

        // ---- Шаг 9: isolation ----------------------------------------------
        System.out.println("\n=== Шаг 9: isolation = READ_COMMITTED ===");
        Article a2 = service.createWithExplicitIsolation(new Article("Isolation Article", "content"));
        System.out.println("Создана: " + a2);

        System.out.println("\n=== Шаг 9: isolation = REPEATABLE_READ + readOnly ===");
        service.findAllRepeatableRead().forEach(a -> System.out.println("  " + a));
    }
}
