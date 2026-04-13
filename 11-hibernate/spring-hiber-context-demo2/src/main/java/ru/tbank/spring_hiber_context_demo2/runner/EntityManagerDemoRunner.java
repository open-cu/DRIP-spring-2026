package ru.tbank.spring_hiber_context_demo2.runner;

import lombok.RequiredArgsConstructor;
import org.hibernate.LazyInitializationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.tbank.spring_hiber_context_demo2.model.Employee;
import ru.tbank.spring_hiber_context_demo2.repository.EntityManagerDemoRepository;

/**
 * ВНИМАНИЕ: УЧЕБНЫЙ ПРИМЕР (DEMO ONLY)
 * <p>
 * Этот класс используется ТОЛЬКО для автоматического запуска демонстрации при старте приложения.
 * <p>
 * В РЕАЛЬНОМ ПРОЕКТЕ архитектура выглядит иначе:
 * <p>
 * HTTP Request
 * ↓
 * [Controller]  ← Принимает запрос
 * ↓
 * [Service]     ← @Transactional, бизнес-логика
 * ↓
 * [Repository]  ← Работа с EntityManager/БД
 * ↓
 * [Database]
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
        // Создаем тестовые данные
        demoRepository.createTestData();
        Employee employeeWithOptionalInit = demoRepository.loadEmployeeWithOptionalInit(true);
        try {
            System.out.println("getDepartment().getId() = " + employeeWithOptionalInit.getDepartment().getId());
            System.out.println("getDepartment().getId() = " + employeeWithOptionalInit.getDepartment().getName());
        } catch (LazyInitializationException e) {
            System.err.println("LazyInitializationException произошло c инициализацией: " + e.getMessage());
        }

        Employee employeeWithoutOptionalInit = demoRepository.loadEmployeeWithOptionalInit(false);
        try {
            System.out.println("getDepartment().getId() = " + employeeWithoutOptionalInit.getDepartment().getId());
            System.out.println("getDepartment().getId() = " + employeeWithoutOptionalInit.getDepartment().getName());
        } catch (LazyInitializationException e) {
            System.err.println("LazyInitializationException произошло БЕЗ инициализации: " + e.getMessage());
        }
        System.out.println("employeeWithOptionalInit = " + employeeWithOptionalInit);
        System.out.println("employeeWithoutOptionalInit = " + employeeWithoutOptionalInit);
    }
}
