package ru.opencu.springcourse.lc3.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleReview;
import ru.opencu.springcourse.lc1.repository.ArticleRepository;
import ru.opencu.springcourse.lc1.repository.ArticleReviewRepository;
import ru.opencu.springcourse.lc3.repository.AuthorJdbcRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleReviewRepository reviewRepository;
    private final AuthorJdbcRepository authorRepository;

    public ArticleService(ArticleRepository articleRepository,
                          ArticleReviewRepository reviewRepository,
                          AuthorJdbcRepository authorRepository) {
        this.articleRepository = articleRepository;
        this.reviewRepository = reviewRepository;
        this.authorRepository = authorRepository;
    }

    // -------------------------------------------------------------------------
    // Базовый CRUD
    // -------------------------------------------------------------------------

    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article create(Article article) {
        try {
            return articleRepository.save(article);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException(
                    "Article with title '%s' already exists".formatted(article.getTitle()), e);
        }
    }

    public void update(Article article) {
        articleRepository.update(article);
    }

    public void delete(long id) {
        articleRepository.delete(id);
    }

    public ArticleReview addReview(long articleId, String text) {
        articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));
        return reviewRepository.save(new ArticleReview(text, articleId));
    }

    // -------------------------------------------------------------------------
    // N+1: три подхода, сравниваем в логах
    // -------------------------------------------------------------------------

    /** Подход 1 (наивный): N+1 запросов — видно в DEBUG-логах. */
    public List<Article> findAllWithAuthors_naive() {
        return ((ru.opencu.springcourse.lc3.repository.ArticleJdbcRepository) articleRepository)
                .findAllWithAuthors_naive();
    }

    /** Подход 2: один JOIN + ResultSetExtractor — в логах ровно 1 запрос. */
    public List<Article> findAllWithAuthors_join() {
        return articleRepository.findAllWithAuthors();
    }

    /** Подход 3: 2 запроса — список статей + один IN-запрос за авторами. */
    public List<Article> findAllWithAuthors_inQuery() {
        List<Article> articles = articleRepository.findAll();
        List<Long> ids = articles.stream().map(Article::getId).toList();
        Map<Long, List<String>> authorsByArticle = authorRepository.findAuthorNamesByArticleIds(ids);
        articles.forEach(a -> a.setAuthors(authorsByArticle.getOrDefault(a.getId(), List.of())));
        return articles;
    }
}
