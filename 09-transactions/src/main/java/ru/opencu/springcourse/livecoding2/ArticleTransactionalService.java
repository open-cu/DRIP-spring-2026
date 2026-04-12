package ru.opencu.springcourse.livecoding2;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.domain.ArticleStatus;
import ru.opencu.springcourse.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Как работает @Transactional:
 *   Spring создаёт прокси-обёртку вокруг бина. При вызове метода через прокси
 *   перехватчик (TransactionInterceptor) открывает/присоединяется к транзакции,
 *   вызывает метод, затем коммитит или откатывает.
 *
 * Демонстрирует:
 *   1. Базовое использование @Transactional на классе и методах
 *   2. rollbackFor и noRollbackFor
 *   3. Проблему самоинвокации (self-invocation) и способы решения
 */
@Service
@Transactional          // ← всем методам по умолчанию: REQUIRED, откат на RuntimeException/Error
public class ArticleTransactionalService {

    private final ArticleRepository articleRepository;
    private final JdbcTemplate jdbc;
    private final ArticlePublishingService publishingService;

    public ArticleTransactionalService(ArticleRepository articleRepository,
                                       JdbcTemplate jdbc,
                                       ArticlePublishingService publishingService) {
        this.articleRepository = articleRepository;
        this.jdbc = jdbc;
        this.publishingService = publishingService;
    }

    // =========================================================================
    // Шаг 1: базовое использование
    // =========================================================================

    /**
     * Транзакция унаследована от класса: REQUIRED, откат на RuntimeException.
     * DuplicateKeyException (RuntimeException) откатит транзакцию автоматически.
     */
    public Article create(Article article) {
        try {
            return articleRepository.save(article);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException(
                    "Article '%s' already exists".formatted(article.getTitle()), e);
        }
    }

    @Transactional(readOnly = true)     // переопределяем readOnly для метода чтения
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Article> findByStatus(ArticleStatus status) {
        return articleRepository.findByStatus(status);
    }

    // =========================================================================
    // Шаг 2: rollbackFor / noRollbackFor
    // =========================================================================

    /**
     * По умолчанию @Transactional откатывается только на RuntimeException и Error.
     * Checked-исключения НЕ вызывают откат без явного rollbackFor.
     *
     * rollbackFor = Exception.class → откат и на checked-исключения.
     */
    @Transactional(rollbackFor = Exception.class)
    public void importFromCsv(String csvContent) throws Exception {
        articleRepository.save(new Article("Imported", csvContent));
        if (csvContent.isBlank()) {
            throw new Exception("CSV is empty");  // откатит транзакцию (checked!)
        }

        // rollbackFor = Exception.class → откат и на checked-исключения
    }

    /**
     * noRollbackFor — исключение НЕ откатывает транзакцию.
     * Используется когда нужно зафиксировать часть работы (например, аудит-лог)
     * даже при "мягкой" ошибке.
     */
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void updateWithLogging(long id, String newTitle) {
        jdbc.update("INSERT INTO outbox_event (event_type, payload) VALUES (?, ?)",
                "UPDATE_ATTEMPT", "id=" + id);
        if (newTitle.isBlank()) {
            throw new IllegalArgumentException("title is blank");
            // исключение НЕ откатит транзакцию — лог в outbox_event останется
        }
        jdbc.update("UPDATE article SET title = ? WHERE id = ?", newTitle, id);
    }

    // =========================================================================
    // Шаг 3: проблема самоинвокации (self-invocation)
    // =========================================================================

    /**
     * ПРОБЛЕМА: this.publishArticle() — вызов не через прокси, аннотация ИГНОРИРУЕТСЯ.
     *
     * @Transactional работает только при вызове через прокси (через другой бин).
     * При this.method() прокси обходится → новая транзакция для publishArticle
     * создана НЕ будет — используется транзакция текущего метода.
     */
    public void publishAllDrafts_BROKEN() {
        List<Article> drafts = articleRepository.findByStatus(ArticleStatus.DRAFT);
        for (Article draft : drafts) {
            publishArticle(draft.getId());  // ← self-invocation! прокси обходится
        }
    }

    @Transactional
    public void publishArticle(long id) {
        articleRepository.updateStatus(id, ArticleStatus.PUBLISHED);
    }

    /**
     * РЕШЕНИЕ 1 (предпочтительное): вызов через другой бин (ArticlePublishingService).
     * Вызов идёт через прокси → @Transactional применяется корректно.
     *
     * РЕШЕНИЕ 2: внедрить себя через ApplicationContext (антипаттерн).
     * РЕШЕНИЕ 3: заменить на программный TransactionTemplate.
     */
    public void publishAllDrafts_FIXED() {
        List<Article> drafts = articleRepository.findByStatus(ArticleStatus.DRAFT);
        for (Article draft : drafts) {
            publishingService.publishArticle(draft.getId());  // ← через другой бин → прокси
        }
    }
}
