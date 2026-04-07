package ru.opencu.springcourse.repository;

import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Optional<Article> findById(long id);
    List<Article> findAll();
    List<Article> findByStatus(ArticleStatus status);
    Article save(Article article);
    void updateStatus(long id, ArticleStatus status);
    void delete(long id);
}
