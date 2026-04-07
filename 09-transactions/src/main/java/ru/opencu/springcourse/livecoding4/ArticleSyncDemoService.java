package ru.opencu.springcourse.livecoding4;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.opencu.springcourse.domain.Article;
import ru.opencu.springcourse.repository.ArticleRepository;

/**
 * TransactionSynchronization и TransactionSynchronizationManager.
 *
 * TransactionSynchronization — низкоуровневый колбэк-интерфейс для хуков транзакции.
 * Альтернатива @TransactionalEventListener без событий.
 *
 * Применяется когда:
 *   — нужны хуки без доменных событий (внутри репозитория, аспекта)
 *   — нужен доступ к текущему TransactionSynchronizationManager
 *   — пишется инфраструктурный код (кэш-менеджер, аудит-аспект)
 *
 * TransactionSynchronizationManager — статический реестр текущего состояния транзакции:
 *   isActualTransactionActive()          — есть ли активная транзакция
 *   isCurrentTransactionReadOnly()       — readOnly?
 *   getCurrentTransactionName()          — имя текущей транзакции
 *   isSynchronizationActive()            — зарегистрированы ли синхронизации
 *   registerSynchronization(sync)        — зарегистрировать колбэк
 *   bindResource(key, value)             — привязать ресурс к транзакции (ThreadLocal)
 *   getResource(key)                     — получить привязанный ресурс
 */
@Service
public class ArticleSyncDemoService {

    private final ArticleRepository articleRepository;

    public ArticleSyncDemoService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // =========================================================================
    // Шаг 2а: registerSynchronization — регистрация колбэков вручную
    // =========================================================================

    @Transactional
    public Article createWithSynchronization(Article article) {
        Article saved = articleRepository.save(article);

        // Регистрируем колбэки для ТЕКУЩЕЙ транзакции (ThreadLocal)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            /**
             * Вызывается непосредственно перед коммитом.
             * Может бросить исключение — тогда транзакция откатится.
             * readOnly = true если транзакция была помечена как readOnly.
             */
            @Override
            public void beforeCommit(boolean readOnly) {
                System.out.println("[Sync] beforeCommit: readOnly=" + readOnly
                        + ", article_id=" + saved.getId());
            }

            /**
             * Вызывается перед завершением транзакции (перед commit или rollback).
             * Нельзя вызывать операции с БД — соединение уже в процессе освобождения.
             */
            @Override
            public void beforeCompletion() {
                System.out.println("[Sync] beforeCompletion: транзакция завершается");
            }

            /**
             * Вызывается после успешного коммита.
             * Аналог @TransactionalEventListener(AFTER_COMMIT).
             * Нет активной транзакции — для работы с БД нужна новая транзакция.
             */
            @Override
            public void afterCommit() {
                System.out.println("[Sync] afterCommit: статья " + saved.getId()
                        + " зафиксирована — безопасно уведомлять внешние системы");
            }

            /**
             * Вызывается после коммита ИЛИ отката.
             * status: STATUS_COMMITTED, STATUS_ROLLED_BACK, STATUS_UNKNOWN
             * Аналог finally — выполняется всегда.
             */
            @Override
            public void afterCompletion(int status) {
                String outcome = switch (status) {
                    case STATUS_COMMITTED   -> "COMMITTED";
                    case STATUS_ROLLED_BACK -> "ROLLED_BACK";
                    default                 -> "UNKNOWN";
                };
                System.out.println("[Sync] afterCompletion: " + outcome);
            }
        });

        return saved;
    }

    // =========================================================================
    // Шаг 2б: TransactionSynchronizationManager — запрос состояния транзакции
    // =========================================================================

    @Transactional
    public void demonstrateTxState() {
        System.out.println("Active TX:  " + TransactionSynchronizationManager.isActualTransactionActive());
        System.out.println("TX name:    " + TransactionSynchronizationManager.getCurrentTransactionName());
        System.out.println("Read-only:  " + TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        System.out.println("Sync active:" + TransactionSynchronizationManager.isSynchronizationActive());
    }

    // =========================================================================
    // Шаг 2в: bindResource / getResource — привязка ресурсов к транзакции
    // =========================================================================

    private static final String CACHE_KEY = "articleCache";

    /**
     * bindResource привязывает произвольный объект к текущей транзакции (ThreadLocal).
     * Полезно для передачи данных между слоями без аргументов метода.
     * Освобождать ресурс нужно в afterCompletion во избежание утечек.
     */
    @Transactional
    public void createWithResourceBinding(Article article) {
        // привязываем "кэш" к транзакции
        TransactionSynchronizationManager.bindResource(CACHE_KEY, article);

        articleRepository.save(article);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                // освобождаем ресурс в любом исходе транзакции
                TransactionSynchronizationManager.unbindResourceIfPossible(CACHE_KEY);
            }
        });
    }
}
