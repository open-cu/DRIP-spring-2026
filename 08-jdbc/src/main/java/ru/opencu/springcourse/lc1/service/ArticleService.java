package ru.opencu.springcourse.lc1.service;

import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleReview;
import ru.opencu.springcourse.lc1.repository.ArticleRepository;
import ru.opencu.springcourse.lc1.repository.ArticleReviewRepository;

import java.util.List;
import java.util.Optional;

/**
 * Намеренно без @Service — это демо-класс, не Spring-бин.
 * Реальный сервис с аннотацией — в lc3.
 */
public class ArticleService {

    private final ArticleRepository articles;
    private final ArticleReviewRepository reviews;

    public ArticleService(ArticleRepository articles, ArticleReviewRepository reviews) {
        this.articles = articles;
        this.reviews = reviews;
    }

    public Optional<Article> findById(long id) {
        return articles.findById(id);
    }

    public List<Article> findAll() {
        return articles.findAll();
    }

    public Article create(Article article) {
        // сюда: валидация, бизнес-правила — без единой строки SQL
        return articles.save(article);
    }

    public void update(Article article) {
        articles.update(article);
    }

    public void delete(long id) {
        articles.delete(id);
    }

    public ArticleReview addReview(long articleId, String text) {
        // бизнес-проверка: статья должна существовать
        articles.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));
        return reviews.save(new ArticleReview(text, articleId));
    }
}
