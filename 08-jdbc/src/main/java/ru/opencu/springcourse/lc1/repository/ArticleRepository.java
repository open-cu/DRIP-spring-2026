package ru.opencu.springcourse.lc1.repository;

import ru.opencu.springcourse.domain.Article;

import java.util.List;
import java.util.Optional;

/**
 * Repository — язык домена: доменные имена методов.
 * Скрывает факт существования БД, оперирует бизнес-понятиями.
 */
public interface ArticleRepository {

    Optional<Article> findById(long id);

    List<Article> findAll();

    /** Загружает статьи вместе со списком авторов. */
    List<Article> findAllWithAuthors();

    Article save(Article article);

    void update(Article article);

    void delete(long id);
}
