package ru.tbank.spring_hiber_context_demo6.repository;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_hiber_context_demo6.model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public List<Employee> findAll() {
                EntityGraph<?> entityGraph = entityManager.getEntityGraph("employees-with-profile-department");

        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e", Employee.class
        );
        query.setHint("jakarta.persistence.fetchgraph", entityGraph);

        return query.getResultList();
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
    @SuppressWarnings("all")

    @Transactional
    public Employee saveEmployee(Employee employee) { // транзакция начинается
        if (employee.getId() == null || employee.getId() == 0) {
            entityManager.persist(employee);
            return employee;
        }
        return entityManager.merge(employee); // транзакция откатывается, если возникла ошибка
    } // транзакция коммитится
}
