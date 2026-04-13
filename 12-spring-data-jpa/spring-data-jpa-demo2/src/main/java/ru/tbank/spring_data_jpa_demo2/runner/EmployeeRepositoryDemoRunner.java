package ru.tbank.spring_data_jpa_demo2.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo2.model.Employee;
import ru.tbank.spring_data_jpa_demo2.repository.EmployeeRepository;

import java.util.Optional;

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
 * - Наша цель — показать работу Spring Data JPA, а не архитектуру веб-приложения.
 * <p>
 * НЕ КОПИРУЙТЕ этот паттерн в домашки и production-код!
 */
@Component
@RequiredArgsConstructor
public class EmployeeRepositoryDemoRunner implements CommandLineRunner {

    private final EmployeeRepository repository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("========== EMPLOYEE REPOSITORY DEMO ==========");

        // Очистим таблицу перед демонстрацией
        repository.deleteAll();

        // Подготовка тестовых данных
        repository.save(new Employee("Anton", 22));
        repository.save(new Employee("Anton", 25));
        repository.save(new Employee("Maria", 22));
        repository.save(new Employee("Ivan", 30));
        repository.save(new Employee("anton", 22));

        System.out.println("\n--- Saved employees ---");
        repository.findAll().forEach(System.out::println);

        // ===== Базовые JpaRepository методы =====
        System.out.println("\n--- save / findById / deleteById demo ---");
        Employee employee = new Employee("Petr", 28);
        System.out.println("Employee before save: " + employee);

        employee = repository.save(employee);
        System.out.println("Employee saved: " + employee);

        Optional<Employee> employeeById = repository.findById(employee.getId());
        System.out.println("Employee after get by id: " + employeeById.orElse(null));

        repository.deleteById(employee.getId());
        System.out.println("Employee deleted by id: " + employee.getId());

        // ===== existsByName =====
        System.out.println("\n--- existsByName ---");
        System.out.println("existsByName('Anton'): " + repository.existsByName("Anton"));
        System.out.println("existsByName('Sergey'): " + repository.existsByName("Sergey"));

        // ===== countByAge =====
        System.out.println("\n--- countByAge ---");
        System.out.println("countByAge(22): " + repository.countByAge(22));
        System.out.println("countByAge(30): " + repository.countByAge(30));

        // ===== findByName =====
        System.out.println("\n--- findByName ---");
        System.out.println("findByName('Anton'):");
        repository.findByName("Anton").forEach(System.out::println);

        // ===== findByAge =====
        System.out.println("\n--- findByAge ---");
        System.out.println("findByAge(22):");
        repository.findByAge(22).forEach(System.out::println);

        // ===== findByNameAndAge =====
        System.out.println("\n--- findByNameAndAge ---");
        System.out.println("findByNameAndAge('Anton', 22):");
        repository.findByNameAndAge("Anton", 22).forEach(System.out::println);

        // ===== findByAgeBetween =====
        System.out.println("\n--- findByAgeBetween ---");
        System.out.println("findByAgeBetween(22, 25):");
        repository.findByAgeBetween(22, 25).forEach(System.out::println);

        // ===== findByNameIgnoreCase =====
        System.out.println("\n--- findByNameIgnoreCase ---");
        System.out.println("findByNameIgnoreCase('anton'):");
        repository.findByNameIgnoreCase("anton").forEach(System.out::println);

        // ===== findByAgeOrderByNameAsc =====
        System.out.println("\n--- findByAgeOrderByNameAsc ---");
        System.out.println("findByAgeOrderByNameAsc(22):");
        repository.findByAgeOrderByNameAsc(22).forEach(System.out::println);

        // ===== deleteByName =====
        System.out.println("\n--- deleteByName ---");
        System.out.println("Before deleteByName('Ivan'):");
        repository.findAll().forEach(System.out::println);

        repository.deleteByName("Ivan");

        System.out.println("After deleteByName('Ivan'):");
        repository.findAll().forEach(System.out::println);
    }
}
