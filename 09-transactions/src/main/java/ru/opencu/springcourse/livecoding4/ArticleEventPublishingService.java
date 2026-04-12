package ru.opencu.springcourse.livecoding4;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.opencu.springcourse.domain.ArticleStatus;
import ru.opencu.springcourse.repository.ArticleRepository;

/**
 * Шаг 1: сервис, публикующий событие внутри транзакции.
 *
 * publishEvent() вызывается синхронно внутри транзакции.
 * Слушатели с @EventListener выполнятся здесь же.
 * Слушатели с @TransactionalEventListener — отложатся до соответствующей фазы.
 */
@Service
public class ArticleEventPublishingService {

    private final ArticleRepository articleRepository;
    private final JdbcTemplate jdbc;
    private final ApplicationEventPublisher eventPublisher;

    public ArticleEventPublishingService(ArticleRepository articleRepository,
                                         JdbcTemplate jdbc,
                                         ApplicationEventPublisher eventPublisher) {
        this.articleRepository = articleRepository;
        this.jdbc = jdbc;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void publishArticle(long id) {
        articleRepository.updateStatus(id, ArticleStatus.PUBLISHED);
        jdbc.update("INSERT INTO outbox_event (event_type, payload) VALUES (?, ?)",
                "ARTICLE_PUBLISHED", "article_id=" + id);

        // Событие публикуется внутри транзакции.
        // @EventListener — выполнится прямо здесь.
        // @TransactionalEventListener — выполнится после commit/rollback.
        eventPublisher.publishEvent(new ArticlePublishedEvent(this, id));

        System.out.println("[Service] publishEvent вызван, транзакция ещё открыта");
    }
}
