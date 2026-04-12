package ru.opencu.springcourse.lc1.repository;

import ru.opencu.springcourse.domain.ArticleReview;

import java.util.List;
import java.util.Optional;

public interface ArticleReviewRepository {

    Optional<ArticleReview> findById(long id);

    List<ArticleReview> findByArticleId(long articleId);

    ArticleReview save(ArticleReview review);

    void delete(long id);
}
