package ru.tbank.spring_hiber_context_demo5.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.tbank.spring_hiber_context_demo5.model.Department;
import ru.tbank.spring_hiber_context_demo5.model.Employee;
import ru.tbank.spring_hiber_context_demo5.repository.DepartmentRepository;
import ru.tbank.spring_hiber_context_demo5.repository.EmployeeRepository;

import java.util.List;

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
public class EmployeeDepartmentRepositoryDemoRunner implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        demoNPlus1();
//        demoDto();
//        demoPagination();
    }

    public void demoNPlus1() {
        Department department1 = new Department("IT");
        departmentRepository.save(department1);
        Department department2 = new Department("HR");
        departmentRepository.save(department2);
        Department department3 = new Department("Marketing");
        departmentRepository.save(department3);
        System.out.println("Saved departments");

        Employee employee1 = new Employee("Anton", 22, department1);
        employeeRepository.save(employee1);
        Employee employee2 = new Employee("Karina", 21, department2);
        employeeRepository.save(employee2);
        Employee employee3 = new Employee("Sergey", 42, department3);
        employeeRepository.save(employee3);
        System.out.println("Saved employees");

        List<Department> all = departmentRepository.findAll();
        System.out.println("All departments: " + all);
    }

    public void demoDto() {
        Department department1 = new Department("IT");
        departmentRepository.save(department1);

        Employee employee1 = new Employee("Anton", 22, department1);
        Employee savedEmployee = employeeRepository.save(employee1);

        employeeRepository.getEmployeeWithDepartmentNameDto(savedEmployee.getId());
    }

    public void demoPagination() {
        Department department1 = new Department("IT");
        departmentRepository.save(department1);
        Department department2 = new Department("HR");
        departmentRepository.save(department2);
        Department department3 = new Department("Marketing");
        departmentRepository.save(department3);
        System.out.println("Saved departments");

        Employee employee1 = new Employee("Anton", 22, department1);
        employeeRepository.save(employee1);
        Employee employee2 = new Employee("Karina", 21, department2);
        employeeRepository.save(employee2);
        Employee employee3 = new Employee("Sergey", 42, department3);
        employeeRepository.save(employee3);
        System.out.println("Saved employees");

       departmentRepository.findDepartmentsWithEmployees(1, 2);
    }
}
