package ru.tbank.spring_data_jpa_demo3.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo3.model.Department;
import ru.tbank.spring_data_jpa_demo3.model.Employee;
import ru.tbank.spring_data_jpa_demo3.repository.DepartmentRepository;
import ru.tbank.spring_data_jpa_demo3.repository.EmployeeRepository;

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
        System.out.println("========== EMPLOYEE QUERY REPOSITORY DEMO ==========");

        // Очистка данных
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

        // Создание департаментов
        Department it = new Department("IT");
        Department hr = new Department("HR");
        Department sales = new Department("Sales");

        departmentRepository.save(it);
        departmentRepository.save(hr);
        departmentRepository.save(sales);

        // Создание сотрудников
        Employee e1 = new Employee("Anton", 22, new BigDecimal("1000"));
        e1.setDepartment(it);

        Employee e2 = new Employee("Maria", 35, new BigDecimal("2500"));
        e2.setDepartment(it);

        Employee e3 = new Employee("Ivan", 19, new BigDecimal("1500"));
        e3.setDepartment(hr);

        Employee e4 = new Employee("Olga", 17, new BigDecimal("900"));
        e4.setDepartment(hr);

        Employee e5 = new Employee("Petr", 28, new BigDecimal("3000"));
        e5.setDepartment(sales);

        Employee e6 = new Employee("Sergey", 41, new BigDecimal("1800"));
        e6.setDepartment(it);

        employeeRepository.saveAll(List.of(e1, e2, e3, e4, e5, e6));

        System.out.println("\n--- All saved employees ---");
        employeeRepository.findAll().forEach(e ->
                System.out.println(e + ", department=" + e.getDepartment().getName() + ", salary=" + e.getSalary())
        );

        // ===== findAllOrderedByAgeAsc =====
        System.out.println("\n--- findAllOrderedByAgeAsc() ---");
        employeeRepository.findAllOrderedByAgeAsc().forEach(e ->
                System.out.println(e + ", department=" + e.getDepartment().getName() + ", salary=" + e.getSalary())
        );

        // ===== findByDepartmentAndSalary =====
        System.out.println("\n--- findByDepartmentAndSalary('IT', 1500) ---");
        employeeRepository.findByDepartmentAndSalary("IT", new BigDecimal("1500")).forEach(e ->
                System.out.println(e + ", department=" + e.getDepartment().getName() + ", salary=" + e.getSalary())
        );

        // ===== findByDepartmentAndSalaryNativeSQL =====
        System.out.println("\n--- findByDepartmentAndSalaryNativeSQL('IT', 1500) ---");
        employeeRepository.findByDepartmentAndSalaryNativeSQL("IT", new BigDecimal("1500")).forEach(e ->
                System.out.println(e + ", department=" + e.getDepartment().getName() + ", salary=" + e.getSalary())
        );

        // ===== UPDATE DEMO =====
        System.out.println("\n--- updateSalaryByName('Anton', 9999) ---");
        int updatedRows = employeeRepository.updateSalaryByName("Anton", new BigDecimal("9999"));
        System.out.println("Updated rows: " + updatedRows);

        System.out.println("\n--- Employees after salary update ---");
        employeeRepository.findAll().forEach(e ->
                System.out.println(e + ", department=" + e.getDepartment().getName() + ", salary=" + e.getSalary())
        );

        System.out.println("\n--- findByDepartmentAndSalary('IT', 1500) after update ---");
        employeeRepository.findByDepartmentAndSalary("IT", new BigDecimal("1500")).forEach(e ->
                System.out.println(e + ", department=" + e.getDepartment().getName() + ", salary=" + e.getSalary())
        );
    }
}
