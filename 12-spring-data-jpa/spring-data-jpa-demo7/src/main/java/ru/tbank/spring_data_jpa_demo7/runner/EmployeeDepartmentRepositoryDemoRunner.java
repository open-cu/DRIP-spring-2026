package ru.tbank.spring_data_jpa_demo7.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo7.model.Department;
import ru.tbank.spring_data_jpa_demo7.model.Employee;
import ru.tbank.spring_data_jpa_demo7.repository.DepartmentRepository;
import ru.tbank.spring_data_jpa_demo7.repository.EmployeeRepository;
import ru.tbank.spring_data_jpa_demo7.utility.EmployeeSpecifications;

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
        System.out.println("========== SPECIFICATION DEMO START ==========");

        prepareData();

        demoSimpleSpecification();
        demoCombinedSpecification();
        demoSpecificationWithPagination();
        demoSpecificationCountAndExists();
    }

    private void prepareData() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

        Department it = new Department("IT");
        Department hr = new Department("HR");
        Department sales = new Department("Sales");

        it.addEmployee(new Employee("Anton", 22, new BigDecimal("1000")));
        it.addEmployee(new Employee("Maria", 35, new BigDecimal("2500")));
        it.addEmployee(new Employee("Sergey", 41, new BigDecimal("1800")));

        hr.addEmployee(new Employee("Ivan", 28, new BigDecimal("1500")));
        hr.addEmployee(new Employee("Olga", 31, new BigDecimal("1700")));

        sales.addEmployee(new Employee("Petr", 45, new BigDecimal("3200")));

        departmentRepository.saveAll(List.of(it, hr, sales));
    }

    public void demoSimpleSpecification() {
        System.out.println("\n--- Simple Specification: age > 30 ---");

        Specification<Employee> spec = EmployeeSpecifications.ageGreaterThan(30);

        List<Employee> employees = employeeRepository.findAll(spec);
        employees.forEach(this::printEmployee);
    }

    public void demoCombinedSpecification() {
        System.out.println("\n--- Combined Specification: department = IT AND salary > 1500 AND name contains 'a' ---");

        Specification<Employee> spec = Specification.allOf(
                EmployeeSpecifications.departmentNameEquals("IT"),
                EmployeeSpecifications.salaryGreaterThan(new BigDecimal("1500")),
                EmployeeSpecifications.nameContainsIgnoreCase("a")
        );

        List<Employee> employees = employeeRepository.findAll(spec);
        employees.forEach(this::printEmployee);
    }

    public void demoSpecificationWithPagination() {
        System.out.println("\n--- Specification with pagination: age > 20, page 0 size 2 sort by age asc ---");

        Specification<Employee> spec = EmployeeSpecifications.ageGreaterThan(20);

        Pageable pageable = PageRequest.of(0, 2, Sort.by("age").ascending());
        Page<Employee> page = employeeRepository.findAll(spec, pageable);

        System.out.println("Page number      : " + page.getNumber());
        System.out.println("Page size        : " + page.getSize());
        System.out.println("Total elements   : " + page.getTotalElements());
        System.out.println("Total pages      : " + page.getTotalPages());
        System.out.println("Elements on page : " + page.getNumberOfElements());

        page.getContent().forEach(this::printEmployee);
    }

    public void demoSpecificationCountAndExists() {
        System.out.println("\n--- Specification count / exists ---");

        Specification<Employee> countSpec = EmployeeSpecifications.departmentNameEquals("HR");

        long count = employeeRepository.count(countSpec);
        System.out.println("Count employees in HR: " + count);

        Specification<Employee> existsSpec = EmployeeSpecifications.hasName("Anton");

        boolean exists = employeeRepository.exists(existsSpec);
        System.out.println("Exists employee with name='Anton': " + exists);
    }

    private void printEmployee(Employee employee) {
        System.out.println("id=" + employee.getId()
                + ", name=" + employee.getName()
                + ", age=" + employee.getAge()
                + ", salary=" + employee.getSalary()
                + ", department=" + (employee.getDepartment() != null ? employee.getDepartment().getName() : null));
    }
}