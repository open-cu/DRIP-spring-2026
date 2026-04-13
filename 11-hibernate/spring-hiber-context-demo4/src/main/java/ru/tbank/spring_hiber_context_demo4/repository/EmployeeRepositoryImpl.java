package ru.tbank.spring_hiber_context_demo4.repository;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_hiber_context_demo4.dto.EmployeeAge;
import ru.tbank.spring_hiber_context_demo4.model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

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
    @Override
    public Employee save(Employee employee) {
        if (employee.getId() == null || employee.getId() == 0) {
            entityManager.persist(employee);
            return employee;
        }
        return entityManager.merge(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(
                entityManager.find(Employee.class, id)
        );
    }

    @Transactional
    @Override
    public Employee updateEmployeeName(Long id, String newName) {
        Employee employee = entityManager.find(Employee.class, id);
        if (employee != null) {
            employee.setName(newName); // dirty checking выполнит UPDATE
        }
        // flush произойдет при коммите
        return employee;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Employee employee = entityManager.find(Employee.class, id);
        if (employee != null) {
            entityManager.remove(employee);
        } else {
            throw new EntityNotFoundException("Employee with id " + id + " not found");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeAge> getEmployeeAge() {
        String jpql = """
                SELECT new ru.tbank.spring_hiber_context_demo4.dto.EmployeeAge(e.name, e.age)
                FROM Employee e
                WHERE e.age > :minAge
                """;

        TypedQuery<EmployeeAge> query = entityManager.createQuery(jpql, EmployeeAge.class);
        query.setParameter("minAge", 18);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tuple> findEmployeeStatsByDepartmentIdAsTuple(Long deptId) {
        String jpql = """
                SELECT COUNT(e) as employeeCount,
                       AVG(e.age) as avgAge,
                       MAX(e.age) as maxAge
                FROM Employee e
                WHERE e.department.id = :deptId
                """;

        return entityManager.createQuery(jpql, Tuple.class)
                .setParameter("deptId", deptId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tuple> findDepartmentStatsWithHighAvgAgeAsTuple() {
        String jpql = """
                SELECT d.name as deptName,
                       COUNT(e) as employeeCount,
                       AVG(e.age) as avgAge
                FROM Employee e
                JOIN e.department d
                GROUP BY d.name
                HAVING AVG(e.age) > 40
                """;

        return entityManager.createQuery(jpql, Tuple.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Employee> findEmployeesWithNoProjects() {
        String jpql = """
                SELECT e
                FROM Employee e
                LEFT JOIN e.projects p
                WHERE p IS NULL
                """;

        return entityManager.createQuery(jpql, Employee.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Employee> findEmployeesWithDepartmentNameLike() {
        String jpql = """
                SELECT e
                FROM Employee e
                JOIN FETCH e.department d
                WHERE d.name LIKE :deptName
                """;

        return entityManager.createQuery(jpql, Employee.class)
                .setParameter("deptName", "IT")
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Employee> findEmployeesWithAgeAboveAverageInDepartment() {
        String jpql = """
                SELECT e FROM Employee e
                WHERE e.age > (
                    SELECT AVG(e2.age)
                    FROM Employee e2
                    WHERE e2.department = e.department
                )
                """;

        return entityManager.createQuery(jpql, Employee.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Employee> findEmployeesWithAgeGreaterThanAnyInHR() {
        String jpql = """
                SELECT e FROM Employee e
                WHERE e.age > ANY (
                    SELECT e2.age
                    FROM Employee e2
                    JOIN e2.department d
                    WHERE d.name = 'HR'
                )
                """;

        return entityManager.createQuery(jpql, Employee.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Employee> findEmployeesWithAgeGreaterThanAllInHR() {
        String jpql = """
                SELECT e FROM Employee e
                WHERE e.age > ALL (
                    SELECT e2.age
                    FROM Employee e2
                    JOIN e2.department d
                    WHERE d.name = 'HR'
                )
                """;

        return entityManager.createQuery(jpql, Employee.class)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findByName(String name) {
        String jpql = """
                SELECT e
                FROM Employee e
                WHERE e.name = :name
                """;

        TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void updateAgeStale(Long id, Integer age) {
        String jpql = """
                UPDATE Employee e
                SET e.age = :age
                WHERE e.id = :id
                """;

        Query query = entityManager.createQuery(jpql);
        query.setParameter("age", age);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Transactional
    @Override
    public void deleteEmployeeByNameStale(String name) {
        String jpql = """
                DELETE FROM Employee e
                WHERE e.name = :name
                """;

        Query query = entityManager.createQuery(jpql);
        query.setParameter("name", name);
        query.executeUpdate();
    }

    @Transactional
    @Override
    public void demoStaleStateUpdate(Long employeeId) {
        Employee e = entityManager.find(Employee.class, employeeId);
        System.out.println("До: " + e.getAge());

//        entityManager.createQuery("""
//                        UPDATE Employee e
//                        SET e.age = 40
//                        WHERE e.id = :id
//                        """)
//                .setParameter("id", employeeId)
//                .executeUpdate();

        // find вернет ТУ ЖЕ управляемую ссылку (без похода в БД)
        Employee sameRef = entityManager.find(Employee.class, employeeId);
        System.out.println("После update, без clear: " + sameRef.getAge()); // все еще старое значение

        // Очистим контекст и перечитаем из БД
        entityManager.clear(); // для примера, используйте метод осознанно тк контекст будет сброшен
        Employee reloaded = entityManager.find(Employee.class, employeeId);
        System.out.println("После clear и find: " + reloaded.getAge()); // уже 40
    }

    @Transactional
    @Override
    public void demoStaleStateDelete(Long employeeId) {
        // 1. Загружаем сущность в Persistence Context (Managed)
        Employee e = entityManager.find(Employee.class, employeeId);
        System.out.println("1. Найдено в контексте: " + e); // e != null

        // 2. Выполняем массовое удаление через JPQL (мимо контекста!)
        int deleted = entityManager.createQuery("""
                    DELETE FROM Employee e
                    WHERE e.id = :id
                    """)
                .setParameter("id", employeeId)
                .executeUpdate();
        System.out.println("2. Удалено строк в БД: " + deleted);

        // 3. ПРОВЕРКА: Объект всё ещё в памяти!
        // Hibernate не знает об удалении, контекст не обновился
        System.out.println("3. Объект в контексте после DELETE: " + e);

        // 4. find() вернёт ТУ ЖЕ ссылку из кэша (без запроса в БД)
        Employee sameRef = entityManager.find(Employee.class, employeeId);
        System.out.println("4. find() вернул ту же ссылку: " + (e == sameRef)); // true

        // 5. Очищаем контекст и проверяем реальное состояние БД
        entityManager.clear();
        Employee reloaded = entityManager.find(Employee.class, employeeId);
        System.out.println("5. После clear(): " + reloaded); // null (в БД удалено)
    }
}
