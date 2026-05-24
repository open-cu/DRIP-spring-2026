package ru.tbank.spring_data_jpa_demo6.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo6.model.Department;
import ru.tbank.spring_data_jpa_demo6.model.Employee;
import ru.tbank.spring_data_jpa_demo6.repository.DepartmentRepository;
import ru.tbank.spring_data_jpa_demo6.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.util.List;

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
public class EmployeeDepartmentRepositoryDemoRunner implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("========== QUERY BY EXAMPLE DEMO ==========");
        prepareData();

        demoFindAllByExample();
        demoFindAllByExampleWithMatcher();
        demoFindOneByExample();
        demoExistsByExample();
        demoCountByExample();
    }

    private void prepareData() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

        Department it = new Department("IT");
        Department hr = new Department("HR");

        it.addEmployee(new Employee("Anton", 22, new BigDecimal("1000")));
        it.addEmployee(new Employee("anton", 25, new BigDecimal("2000")));
        it.addEmployee(new Employee("Maria", 35, new BigDecimal("2500")));
        it.addEmployee(new Employee("Sergey", 41, new BigDecimal("1800")));

        hr.addEmployee(new Employee("Ivan", 28, new BigDecimal("1500")));
        hr.addEmployee(new Employee("Olga", 31, new BigDecimal("1700")));

        departmentRepository.saveAll(List.of(it, hr));
    }

    public void demoFindAllByExample() {
        System.out.println("\n--- QBE: findAll by exact name ---");

        Employee probe = new Employee();
        probe.setName("Anton");

        Example<Employee> example = Example.of(probe);

        List<Employee> result = employeeRepository.findAll(example);
        result.forEach(this::printEmployee);
    }

    public void demoFindAllByExampleWithMatcher() {
        System.out.println("\n--- QBE: findAll with matcher (ignore case + contains) ---");

        Employee probe = new Employee();
        probe.setName("ant");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase("name")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Employee> example = Example.of(probe, matcher);

        List<Employee> result = employeeRepository.findAll(example);
        result.forEach(this::printEmployee);
    }

    public void demoFindOneByExample() {
        System.out.println("\n--- QBE: findOne by name + age ---");

        Employee probe = new Employee();
        probe.setName("Maria");
        probe.setAge(35);

        Example<Employee> example = Example.of(probe);

        employeeRepository.findOne(example)
                .ifPresentOrElse(
                        this::printEmployee,
                        () -> System.out.println("Employee not found")
                );
    }

    public void demoExistsByExample() {
        System.out.println("\n--- QBE: exists by age ---");

        Employee probe = new Employee();
        probe.setAge(28);

        Example<Employee> example = Example.of(probe);

        boolean exists = employeeRepository.exists(example);
        System.out.println("Exists employee with age=28: " + exists);
    }

    public void demoCountByExample() {
        System.out.println("\n--- QBE: count by department ignored, name contains 'a' ---");

        Employee probe = new Employee();
        probe.setName("a");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase("name")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("age", "salary", "department");

        Example<Employee> example = Example.of(probe, matcher);

        long count = employeeRepository.count(example);
        System.out.println("Count employees where name contains 'a': " + count);
    }

    private void printEmployee(Employee employee) {
        System.out.println("id=" + employee.getId()
                + ", name=" + employee.getName()
                + ", age=" + employee.getAge()
                + ", salary=" + employee.getSalary()
                + ", department=" + (employee.getDepartment() != null ? employee.getDepartment().getName() : null));
    }
}