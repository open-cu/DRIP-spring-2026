package ru.tbank.spring_data_jpa_demo5.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo5.model.Department;
import ru.tbank.spring_data_jpa_demo5.model.Employee;
import ru.tbank.spring_data_jpa_demo5.projection.EmployeeProjection;
import ru.tbank.spring_data_jpa_demo5.projection.EmployeeWithDepartmentSummaryProjection;
import ru.tbank.spring_data_jpa_demo5.repository.DepartmentRepository;
import ru.tbank.spring_data_jpa_demo5.repository.EmployeeRepository;

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
        prepareData();
//        demoInterfaceProjection();
//        demoRecordProjection();
        demoDynamicProjection();
    }

    private void prepareData() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

        Department it = new Department("IT");
        Department hr = new Department("HR");

        it.addEmployee(new Employee("Anton", 22, new BigDecimal("1000")));
        it.addEmployee(new Employee("Maria", 35, new BigDecimal("2500")));
        it.addEmployee(new Employee("Sergey", 41, new BigDecimal("1800")));
        hr.addEmployee(new Employee("Ivan", 28, new BigDecimal("1500")));
        hr.addEmployee(new Employee("Olga", 31, new BigDecimal("1700")));

        departmentRepository.saveAll(List.of(it, hr));
    }

    public void demoInterfaceProjection() {
        System.out.println("\n--- Interface projection demo ---");

        Pageable pageable = PageRequest.of(0, 3, Sort.by("age").ascending());

        Page<EmployeeWithDepartmentSummaryProjection> projections = employeeRepository.findAllBy(pageable);

        System.out.println("Requested pageable: " + pageable);
        System.out.println("Returned projection count: " + projections.getSize());

        projections.forEach(projection ->
                System.out.println("name=" + projection.getName()
                        + ", age=" + projection.getAge()
                        + ", department=" + projection.getDepartment().getName()
                        + ", nameWithDepartmentName=" + projection.getNameWithDepartmentName()
                        + ", nameWIthAge=" + projection.getNameWthAge()
                )
        );
    }

    public void demoRecordProjection() {
        System.out.println("\n--- Record projection demo ---");

        List<EmployeeProjection> projections = employeeRepository.findByDepartmentName("IT");

        System.out.println("Employees from IT department:");
        projections.forEach(projection ->
                System.out.println("name=" + projection.name()
                        + ", salary=" + projection.salary())
        );
    }

    public void demoDynamicProjection() {
        System.out.println("\n--- Dynamic projection demo ---");

        // 1. Полная сущность Employee
        System.out.println("\n1) Full Employee:");
        List<Employee> employees = employeeRepository.findByDepartmentName("IT", Employee.class);
        employees.forEach(employee ->
                System.out.println("id=" + employee.getId()
                        + ", name=" + employee.getName()
                        + ", age=" + employee.getAge()
                        + ", salary=" + employee.getSalary()
                        + ", department=" + employee.getDepartment().getName())
        );

        // 2. Простая проекция EmployeeProjection
        System.out.println("\n2) EmployeeProjection:");
        List<EmployeeProjection> employeeProjections =
                employeeRepository.findByDepartmentName("IT", EmployeeProjection.class);

        employeeProjections.forEach(projection ->
                System.out.println("name=" + projection.name()
                        + ", salary=" + projection.salary())
        );

        // 3. Расширенная интерфейсная проекция EmployeeWithDepartmentSummaryProjection
        System.out.println("\n3) EmployeeWithDepartmentSummaryProjection:");
        List<EmployeeWithDepartmentSummaryProjection> summaryProjections =
                employeeRepository.findByDepartmentName("IT", EmployeeWithDepartmentSummaryProjection.class);

        summaryProjections.forEach(projection ->
                System.out.println("name=" + projection.getName()
                        + ", age=" + projection.getAge()
                        + ", department=" + projection.getDepartment().getName()
                        + ", nameWithDepartmentName=" + projection.getNameWithDepartmentName()
                        + ", nameWithAge=" + projection.getNameWthAge())
        );
    }
}