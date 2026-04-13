package ru.tbank.spring_data_jpa_demo8.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.LazyInitializationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo8.model.Department;
import ru.tbank.spring_data_jpa_demo8.model.Employee;
import ru.tbank.spring_data_jpa_demo8.repository.DepartmentRepository;
import ru.tbank.spring_data_jpa_demo8.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaDemoProblemService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

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

    // обращаемся к прокси департамента вне транзакции, получаем LazyInitializationException
    // решаем с помощью JOIN FETCH или @EntityGraph
    public void demoLazyInitializationException() {
        System.out.println("\n--- Example 1: LazyInitializationException ---");

        Employee employee = employeeRepository.findAll().get(0);

        System.out.println("Employee loaded: " + employee.getName());
        System.out.println("Now trying to access lazy department outside transaction...");

        try {
            System.out.println("Department name: " + employee.getDepartment().getName());
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException: " + e.getMessage());
        }
    }

    // получаем сущность сразу со связанной сущностью
    public void demoLazyInitializationExceptionSolved() {
        System.out.println("\n--- Example 1b: LazyInitializationException solved with @EntityGraph ---");

        Long employeeId = employeeRepository.findAll().get(0).getId();

        Employee employee = employeeRepository.findWithDepartmentById(employeeId)
                .orElseThrow();

        System.out.println("Employee loaded: " + employee.getName());
        System.out.println("Department was loaded together with employee");

        // Здесь department уже загружен, поэтому обращения вне транзакции не упадут
        System.out.println("Department name: " + employee.getDepartment().getName());
    }

    // получаем сотрудников и для каждого из них запрашиваем N департаментов
    // решаем с помощью @EntityGraph
    @Transactional(readOnly = true)
    public void demoNPlusOne() {
        System.out.println("\n--- Example 2: N+1 ---");

        List<Employee> employees = employeeRepository.findAllBy();

        System.out.println("Employees loaded: " + employees.size());
        System.out.println("Accessing department in loop...");

        for (Employee employee : employees) {
            System.out.println(employee.getName() + " -> " + employee.getDepartment().getName());
        }
    }

    // вместо N отдельных догрузок — один SQL запрос с JOIN
    @Transactional(readOnly = true)
    public void demoEntityGraph() {
        System.out.println("\n--- Example 3: @EntityGraph ---");

        List<Employee> employees = employeeRepository.findAllWithDepartment();

        System.out.println("Employees loaded: " + employees.size());
        System.out.println("Accessing department in loop...");

        for (Employee employee : employees) {
            System.out.println(employee.getName() + " -> " + employee.getDepartment().getName());
        }
    }

    // сортировка происходит в памяти!
    // firstResult/maxResults specified with collection fetch; applying in memory
    @Transactional(readOnly = true)
    public void demoPaginationWithCollectionFetchJoin() {
        System.out.println("\n--- Example 4: Pagination + collection fetch join ---");

        Pageable pageable = PageRequest.of(0, 2, Sort.by("name"));

        Page<Department> page = departmentRepository.findAllWithEmployeesFetchJoin(pageable);

        System.out.println("Page size requested: " + pageable.getPageSize());
        System.out.println("Total elements: " + page.getTotalElements());
        System.out.println("Departments returned: " + page.getContent().size());

        for (Department department : page.getContent()) {
            System.out.println("Department: " + department.getName()
                    + ", employees count: " + department.getEmployees().size());
        }
    }

    // отдельно получаем департаменты
    // затем получаем по нужным id JOIN с сотрудниками
    @Transactional(readOnly = true)
    public void demoPaginationCorrect() {
        System.out.println("\n--- Example 4b: Correct pagination approach ---");

        Pageable pageable = PageRequest.of(0, 2, Sort.by("name"));
        Page<Department> page = departmentRepository.findAllBy(pageable);

        List<Long> ids = page.getContent().stream()
                .map(Department::getId)
                .toList();

        List<Department> departments = departmentRepository.findByIdIn(ids);

        System.out.println("Departments in page: " + departments.size());
        for (Department department : departments) {
            System.out.println("Department: " + department.getName()
                    + ", employees count: " + department.getEmployees().size());
        }
    }
}
