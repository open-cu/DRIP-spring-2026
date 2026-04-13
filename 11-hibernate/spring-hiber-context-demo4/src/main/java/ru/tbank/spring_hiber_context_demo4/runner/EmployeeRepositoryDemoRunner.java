package ru.tbank.spring_hiber_context_demo4.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.tbank.spring_hiber_context_demo4.dto.EmployeeAge;
import ru.tbank.spring_hiber_context_demo4.model.Employee;
import ru.tbank.spring_hiber_context_demo4.repository.EmployeeRepository;

import java.util.List;
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
 * - Наша цель — показать работу EntityManager, а не архитектуру веб-приложения.
 * <p>
 * НЕ КОПИРУЙТЕ этот паттерн в домашки и production-код!
 */
@Component
@RequiredArgsConstructor
public class EmployeeRepositoryDemoRunner implements CommandLineRunner {

    private final EmployeeRepository repository;

    @Override
    public void run(String... args) throws Exception {
//        demoCreateUpdateDelete();
//        demoProjectionCreate();
        demoStaleStateUpdate();
    }

    private void demoCreateUpdateDelete() {
        Employee employee = new Employee("Anton", 22);
        System.out.println("Employee before save: " + employee);

        employee = repository.save(employee);
        System.out.println("Employee saved: " + employee);

        employee = repository.updateEmployeeName(employee.getId(), "Antoxa");
        System.out.println("Employee after update: " + employee);

        Optional<Employee> byId = repository.findById(employee.getId());
        System.out.println("Employee after get: " + byId.orElse(null));

        repository.deleteById(employee.getId());
    }

    private void demoProjectionCreate() {
        Employee employee = new Employee("Anton", 22);
        System.out.println("Employee before save: " + employee);

        employee = repository.save(employee);
        System.out.println("Employee saved: " + employee);

        List<EmployeeAge> employeeAge = repository.getEmployeeAge();
        System.out.println("Employee age > 18: " + employeeAge);

        repository.deleteById(employee.getId());
    }

    private void demoStaleStateUpdate() {
        Employee employee = new Employee("Anton", 22);
        repository.save(employee);
        repository.demoStaleStateUpdate(employee.getId());
//        repository.demoStaleStateDelete(employee.getId());
    }
}
