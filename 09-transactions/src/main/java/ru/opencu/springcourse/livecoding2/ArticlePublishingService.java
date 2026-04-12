package ru.opencu.springcourse.livecoding2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.opencu.springcourse.domain.ArticleStatus;
import ru.opencu.springcourse.repository.ArticleRepository;

/**
 *
 * publishArticle вынесен в отдельный бин →
 * вызов из ArticleTransactionalService.publishAllDrafts_FIXED() идёт через прокси →
 * @Transactional создаёт новую транзакцию (или присоединяется к существующей).
 */
@Service
public class ArticlePublishingService {

    private final ArticleRepository articleRepository;

    public ArticlePublishingService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public void publishArticle(long id) {
        articleRepository.updateStatus(id, ArticleStatus.PUBLISHED);
    }
}
