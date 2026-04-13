package ru.tbank.spring_data_jpa_demo9.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo9.model.Department;
import ru.tbank.spring_data_jpa_demo9.model.Employee;
import ru.tbank.spring_data_jpa_demo9.repository.DepartmentRepository;
import ru.tbank.spring_data_jpa_demo9.repository.EmployeeRepository;

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

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        prepareData();
        demoCustomRepositoryExtension();
    }

    public void prepareData() {
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
        sales.addEmployee(new Employee("Anna", 29, new BigDecimal("2100")));

        departmentRepository.saveAll(List.of(it, hr, sales));
    }

    public void demoCustomRepositoryExtension() {
        System.out.println("\n--- Example Custom repository extension ---");

        System.out.println("\n1) Search by name contains 'a'");
        List<Employee> result1 = employeeRepository.findEmployeesByFilters("a", null, null);
        result1.forEach(this::printEmployee);

        System.out.println("\n2) Search by minAge = 30");
        List<Employee> result2 = employeeRepository.findEmployeesByFilters(null, 30, null);
        result2.forEach(this::printEmployee);

        System.out.println("\n3) Search by department = IT and minAge = 30");
        List<Employee> result3 = employeeRepository.findEmployeesByFilters(null, 30, "IT");
        result3.forEach(this::printEmployee);

        System.out.println("\n4) Search by name contains 'a', minAge = 25, department = IT");
        List<Employee> result4 = employeeRepository.findEmployeesByFilters("a", 25, "IT");
        result4.forEach(this::printEmployee);
    }

    private void printEmployee(Employee employee) {
        System.out.println("id=" + employee.getId()
                + ", name=" + employee.getName()
                + ", age=" + employee.getAge()
                + ", salary=" + employee.getSalary()
                + ", department=" + (employee.getDepartment() != null ? employee.getDepartment().getName() : null));
    }
}