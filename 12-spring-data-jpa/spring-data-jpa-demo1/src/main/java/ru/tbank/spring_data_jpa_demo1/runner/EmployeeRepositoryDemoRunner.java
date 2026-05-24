package ru.tbank.spring_data_jpa_demo1.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.tbank.spring_data_jpa_demo1.model.Employee;
import ru.tbank.spring_data_jpa_demo1.repository.EmployeeRepository;

import java.util.Optional;

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
 * - Наша цель — показать работу Spring Data JPA, а не архитектуру веб-приложения.
 * <p>
 * НЕ КОПИРУЙТЕ этот паттерн в домашки и production-код!
 */
@Component
@RequiredArgsConstructor
public class EmployeeRepositoryDemoRunner implements CommandLineRunner {

    private final EmployeeRepository repository;

    @Override
    public void run(String... args) throws Exception {
        Employee employee = new Employee("Anton", 22);
        System.out.println("Employee before save: " + employee);

        employee = repository.save(employee);
        System.out.println("Employee saved: " + employee);

        Optional<Employee> employeeById = repository.findById(employee.getId());
        System.out.println("Employee after get: " + employeeById.orElse(null));

        repository.deleteById(employee.getId());
    }
}
