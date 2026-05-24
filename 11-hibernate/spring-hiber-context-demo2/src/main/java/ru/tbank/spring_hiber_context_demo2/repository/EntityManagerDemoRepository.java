package ru.tbank.spring_hiber_context_demo2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_hiber_context_demo2.model.Department;
import ru.tbank.spring_hiber_context_demo2.model.Employee;
import ru.tbank.spring_hiber_context_demo2.model.EmployeeProfile;

@Repository
@RequiredArgsConstructor
public class EntityManagerDemoRepository {

    /**
     * Обратите внимание:
     * 1. @PersistenceContext здесь работает как метка
     *    Технически Spring может внедрить EntityManager через конструктор и без неё
     *    (так как это обычный бин в Spring Boot).
     * 2. Мы оставляем аннотацию намеренно, чтобы:
     *    - Явно показать, что это JPA-компонент (стандарт, а не только Spring)
     *    - Сделать код понятнее для других разработчиков
     * 3. Поле final — это правильно! Lombok создаст конструктор,
     *    и Spring внедрит зависимость через него (конструкторная инъекция).
     */
    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * ВНИМАНИЕ: В реальных приложениях @Transactional должна быть на Service слое!
     * Здесь она используется на уровне методов Repository только для упрощения учебного примера,
     * чтобы не создавать лишние классы-прослойки.
     * Не копируйте этот паттерн в домашки и production-код.
     */
    @Transactional
    public void createTestData() {
        System.out.println("=== ДЕМОНСТРАЦИЯ: Работа метода detach() Employee и Department ===\n");
        System.out.println("Создаем и сохраняем тестовые данные");

        // Создаем и сохраняем тестовые данные
        Department department = new Department();
        department.setName("IT Department");

        Employee employee1 = new Employee("Anton", 22);
        Employee employee2 = new Employee("Kristina", 21);

        employee1.setDepartment(department);
        employee2.setDepartment(department);

        department.getEmployees().add(employee1);
        department.getEmployees().add(employee2);

        entityManager.persist(department);

        Employee emp = entityManager.find(Employee.class, 1L);
        System.out.println("Найден: " + emp);

        Employee emp2 = entityManager.find(Employee.class, 2L);
        System.out.println("Найден: " + emp2);

        Department dep = entityManager.find(Department.class, 1L);
        System.out.println("Найден: " + dep);
    }

    @Transactional
    public Employee loadEmployeeWithOptionalInit(Boolean initRelationInTransaction) {
        Employee employee = entityManager.find(Employee.class, 1L);
        if (initRelationInTransaction) {
            System.out.println("Загружаем связанное поле в транзакции");
            employee.getDepartment().getName();
        }
        return employee; // Возвращаем объект (он станет Detached после выхода из метода)
    }

    @Transactional
    public void demonstrateDirtyChecking() {
        System.out.println("=== DIRTY CHECKING DEMO ===\n");

        Employee employee = new Employee("Anton", 22); // cохраняем сотрудника
        entityManager.persist(employee);
        entityManager.flush();

        employee.setAge(30); // изменяем поле (БЕЗ вызова save/update!)

        entityManager.flush(); // при flush() изменения автоматически попадут в БД

        entityManager.find(Employee.class, employee.getId());
        // Вывод: Employee{id=1, name='Anton', age=30}
    }
}
