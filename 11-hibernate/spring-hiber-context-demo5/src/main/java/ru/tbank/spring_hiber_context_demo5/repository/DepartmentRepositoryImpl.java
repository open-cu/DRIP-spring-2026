package ru.tbank.spring_hiber_context_demo5.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_hiber_context_demo5.model.Department;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

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
    public Department save(Department department) {
        if (department.getId() == null || department.getId() == 0) {
            entityManager.persist(department);
            return department;
        }
        return entityManager.merge(department);
    }

    // решение N+1 через Lazy
//    @Transactional(readOnly = true)
//    @Override
//    public List<Department> findAll() {
//        List<Department> selectedDepartment = entityManager.createQuery(
//                "SELECT d FROM Department d", Department.class)
//                .getResultList();
//        // В prod'e принудительная инициализация через size() или через parent.getNested().getOtherField(),
//        // а так же через Hibernate.initialize() - плохая идея.
//        // В первом случае этот код скорее всего сотрут т.к. он выглядит не нужным,
//        // а во втором, мы на уровне абстракции получаем знание о реализации.
//        // далее посмотрим как не допускать такой проблемы через EntityGraph или DTO
////        for (Department department : selectedDepartment) {
////            department.getEmployees().size(); // тк у нас указан FetchType.LAZY
////                                              // принудительно загружаем все остальное
////                                              // эмулируем FetchType.EAGER
////        }
//        return selectedDepartment;
//    }

    // решение N+1 через EntityGraph
//    @Transactional(readOnly = true)
//    @Override
//    public List<Department> findAll() {
//        EntityGraph<?> entityGraph = entityManager.getEntityGraph("department-with-employees");
//
//        TypedQuery<Department> query = entityManager.createQuery(
//                "SELECT d FROM Department d", Department.class
//        );
//        query.setHint("jakarta.persistence.fetchgraph", entityGraph);
//
//        return query.getResultList();
//    }

    // решение N+1 через JOIN FETCH
//    @Transactional(readOnly = true)
//    @Override
//    public List<Department> findAll() {
//        TypedQuery<Department> query = entityManager.createQuery(
//                "SELECT DISTINCT d FROM Department d JOIN FETCH d.employees", Department.class
//        );
//
//        return query.getResultList();
//    }

//     решение N+1 через @Fetch(SUBSELECT) и @BatchSize
    @Transactional(readOnly = true)
    @Override
    public List<Department> findAll() {
        List<Department> selectedDepartment = entityManager.createQuery(
                "SELECT d FROM Department d", Department.class)
                .getResultList();

        for (Department department : selectedDepartment) {
            department.getEmployees().size(); // тк у нас указан FetchType.LAZY
                                              // принудительно загружаем все остальное
                                              // эмулируем FetchType.EAGER
        }
        return selectedDepartment;
    }


    @Transactional(readOnly = true)
    @Override
    public Department findById(Long id) {
        return entityManager.find(Department.class, id);
    }

    public List<Department> findDepartmentsWithEmployees(int page, int size) {
        TypedQuery<Department> query = entityManager.createQuery(
                """
                SELECT DISTINCT d FROM Department d
                LEFT JOIN FETCH d.employees
                """,
                Department.class
        );

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }
}
