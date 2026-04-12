package ru.opencu.springcourse.livecoding4;

import org.springframework.context.ApplicationEvent;

/**
 * Доменное событие — статья опубликована.
 * Публикуется внутри транзакции через ApplicationEventPublisher.
 * Слушатели получают его в зависимости от фазы (см. ArticleEventListener).
 */
public class ArticlePublishedEvent extends ApplicationEvent {

    private final long articleId;

    public ArticlePublishedEvent(Object source, long articleId) {
        super(source);
        this.articleId = articleId;
    }

    public long getArticleId() {
        return articleId;
    }
}
