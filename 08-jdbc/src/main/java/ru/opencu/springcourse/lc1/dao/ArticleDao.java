package ru.opencu.springcourse.lc1.dao;

import ru.opencu.springcourse.domain.Article;

import java.util.List;
import java.util.Optional;

/**
 * DAO — язык хранилища: технические имена методов.
 * Знает про таблицы, строки, операции CRUD.
 */
public interface ArticleDao {

    Optional<Article> selectById(long id);

    List<Article> selectAll();

    void insert(Article article);

    void update(Article article);

    void deleteById(long id);
}
