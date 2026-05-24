package ru.tbank.spring_data_jpa_demo4.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo4.model.Department;
import ru.tbank.spring_data_jpa_demo4.model.Employee;
import ru.tbank.spring_data_jpa_demo4.repository.DepartmentRepository;
import ru.tbank.spring_data_jpa_demo4.repository.EmployeeRepository;

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
        System.out.println("========== PAGINATION DEMO START ==========");

        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

        Department it = new Department("IT");
        Department hr = new Department("HR");

        it.addEmployee(new Employee("Anton", 22, new BigDecimal("1000")));
        it.addEmployee(new Employee("Maria", 35, new BigDecimal("2500")));
        it.addEmployee(new Employee("Sergey", 41, new BigDecimal("1800")));
        it.addEmployee(new Employee("Ivan", 28, new BigDecimal("2100")));
        it.addEmployee(new Employee("Olga", 31, new BigDecimal("1900")));

        hr.addEmployee(new Employee("Petr", 26, new BigDecimal("1500")));
        hr.addEmployee(new Employee("Anna", 24, new BigDecimal("1700")));

        departmentRepository.saveAll(List.of(it, hr));

        System.out.println("\n--- All IT employees in DB ---");
        employeeRepository.findAll().stream()
                .filter(e -> "IT".equals(e.getDepartment().getName()))
                .forEach(this::printEmployee);

        // page = 0, size = 2, sort by age asc
        Pageable firstPageable = PageRequest.of(0, 2, Sort.by("age").ascending());
        Page<Employee> firstPage = employeeRepository.findByDepartmentName("IT", firstPageable);

        System.out.println("\n--- First page: PageRequest.of(0, 2, Sort.by(\"age\").ascending()) ---");
        printPageInfo(firstPage);
        firstPage.getContent().forEach(this::printEmployee);

        // page = 1, size = 2, sort by age asc
        Pageable secondPageable = PageRequest.of(1, 2, Sort.by("age").ascending());
        Page<Employee> secondPage = employeeRepository.findByDepartmentName("IT", secondPageable);

        System.out.println("\n--- Second page: PageRequest.of(1, 2, Sort.by(\"age\").ascending()) ---");
        printPageInfo(secondPage);
        secondPage.getContent().forEach(this::printEmployee);

        // page = 2, size = 2, sort by age asc
        Pageable thirdPageable = PageRequest.of(2, 2, Sort.by("age").ascending());
        Page<Employee> thirdPage = employeeRepository.findByDepartmentName("IT", thirdPageable);

        System.out.println("\n--- Third page: PageRequest.of(2, 2, Sort.by(\"age\").ascending()) ---");
        printPageInfo(thirdPage);
        thirdPage.getContent().forEach(this::printEmployee);

        // пример сортировки по имени по убыванию
        Pageable sortedByNameDesc = PageRequest.of(0, 3, Sort.by("name").descending());
        Page<Employee> pageByNameDesc = employeeRepository.findByDepartmentName("IT", sortedByNameDesc);

        System.out.println("\n--- First page sorted by name desc: PageRequest.of(0, 3, Sort.by(\"name\").descending()) ---");
        printPageInfo(pageByNameDesc);
        pageByNameDesc.getContent().forEach(this::printEmployee);
    }

    private void printPageInfo(Page<Employee> page) {
        System.out.println("page number       : " + page.getNumber());
        System.out.println("page size         : " + page.getSize());
        System.out.println("elements on page  : " + page.getNumberOfElements());
        System.out.println("total elements    : " + page.getTotalElements());
        System.out.println("total pages       : " + page.getTotalPages());
        System.out.println("is first          : " + page.isFirst());
        System.out.println("is last           : " + page.isLast());
        System.out.println("has next          : " + page.hasNext());
        System.out.println("has previous      : " + page.hasPrevious());
        System.out.println("sort              : " + page.getSort());
    }

    private void printEmployee(Employee e) {
        System.out.println(
                e + ", department=" + e.getDepartment().getName() + ", salary=" + e.getSalary()
        );
    }
}