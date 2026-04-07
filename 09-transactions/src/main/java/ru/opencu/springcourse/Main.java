package ru.opencu.springcourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;
import ru.opencu.springcourse.livecoding1.ArticleProgrammaticTxService;
import ru.opencu.springcourse.livecoding2.ArticleTransactionalService;
import ru.opencu.springcourse.livecoding3.ArticlePropagationService;
import ru.opencu.springcourse.livecoding4.ArticleEventPublishingService;
import ru.opencu.springcourse.livecoding4.ArticleSyncDemoService;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);

        // ЛК#1 — программное управление транзакциями
        ArticleProgrammaticTxService lk1 = ctx.getBean(ArticleProgrammaticTxService.class);
        System.out.println("\n=== ЛК#1: создаём статью через TransactionTemplate ===");
        Article created = lk1.createWithTemplate(new Article("Programmatic TX Article", "content"));
        System.out.println("Создана: " + created);
        System.out.println("Опубликовано: " + lk1.countPublished());

        // ЛК#2 — @Transactional
        ArticleTransactionalService lk2 = ctx.getBean(ArticleTransactionalService.class);
        System.out.println("\n=== ЛК#2: все статьи (readOnly транзакция) ===");
        lk2.findAll().forEach(System.out::println);

        // ЛК#3 — Propagation, ReadOnly, Timeout
        ArticlePropagationService lk3 = ctx.getBean(ArticlePropagationService.class);
        System.out.println("\n=== ЛК#3: создаём статью с аудит-логом (REQUIRES_NEW) ===");
        Article article3 = lk3.create(new Article("Propagation Demo Article", "content"));
        System.out.println("Создана: " + article3);
        System.out.println("Всего статей: " + lk3.countAll());

        // ЛК#4 — TransactionalEventListener + TransactionSynchronization
        ArticleEventPublishingService lk4events = ctx.getBean(ArticleEventPublishingService.class);
        System.out.println("\n=== ЛК#4: публикуем статью с событиями ===");
        lk4events.publishArticle(created.getId());

        ArticleSyncDemoService lk4sync = ctx.getBean(ArticleSyncDemoService.class);
        System.out.println("\n=== ЛК#4: TransactionSynchronization ===");
        lk4sync.demonstrateTxState();
        lk4sync.createWithSynchronization(new Article("Sync Demo Article", "content"));

        System.out.println("\n=== Итого PUBLISHED ===");
        lk3.findByStatus(ArticleStatus.PUBLISHED).forEach(System.out::println);
    }
}
