package ru.tbank.spring_hiber_context_demo1.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.tbank.spring_hiber_context_demo1.repository.EntityManagerDemoRepository;

/**
 * ВНИМАНИЕ: УЧЕБНЫЙ ПРИМЕР (DEMO ONLY)
 * <p>
 * Этот класс используется ТОЛЬКО для автоматического запуска демонстрации при старте приложения.
 * <p>
 * В РЕАЛЬНОМ ПРОЕКТЕ архитектура выглядит иначе:
 * <p>
 *    HTTP Request
 *         ↓
 *    [Controller]  ← Принимает запрос
 *         ↓
 *    [Service]     ← @Transactional, бизнес-логика
 *         ↓
 *    [Repository]  ← Работа с EntityManager/БД
 *         ↓
 *    [Database]
 * <p>
 * Почему здесь иначе?
 * - Мы вызываем Repository напрямую, чтобы не создавать лишние классы-прослойки.
 * - Наша цель — показать работу EntityManager, а не архитектуру веб-приложения.
 * <p>
 * НЕ КОПИРУЙТЕ этот паттерн в домашки и production-код!
 */
@Component
@RequiredArgsConstructor
public class EntityManagerDemoRunner implements CommandLineRunner {

    private final EntityManagerDemoRepository demoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== ДЕМОНСТРАЦИЯ: EntityManager и состояния Employee ===\n");

        demoRepository.demonstratePersist();
        demoRepository.demonstrateFind();
        demoRepository.demonstrateDetachAndMerge();
        demoRepository.demonstrateRemove();
        demoRepository.demonstrateRefresh();
    }
}
