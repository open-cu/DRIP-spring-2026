package ru.opencu.springcourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.lc3.service.ArticleService;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);

        ArticleService service = ctx.getBean(ArticleService.class);

        System.out.println("\n=== Все статьи ===");
        service.findAll().forEach(System.out::println);

        System.out.println("\n=== Создаём новую статью ===");
        Article saved = service.create(new Article("Test Article", "Some text", "Nature"));
        System.out.println("Создана: " + saved);

        System.out.println("\n=== Статьи с авторами (JOIN) ===");
        List<Article> withAuthors = service.findAllWithAuthors_join();
        withAuthors.forEach(System.out::println);

        System.out.println("\n=== Статьи с авторами (IN-запрос) ===");
        service.findAllWithAuthors_inQuery().forEach(System.out::println);
    }
}
