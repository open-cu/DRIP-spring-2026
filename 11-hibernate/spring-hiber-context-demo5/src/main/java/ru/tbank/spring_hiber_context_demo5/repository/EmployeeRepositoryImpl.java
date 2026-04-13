package ru.tbank.spring_hiber_context_demo5.repository;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_hiber_context_demo5.dto.EmployeeWithDepartment;
import ru.tbank.spring_hiber_context_demo5.model.Employee;

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
    public EmployeeWithDepartment getEmployeeWithDepartmentNameDto(Long id) {
        Employee employee = entityManager.find(Employee.class, id);

        return new EmployeeWithDepartment(
                employee.getName(),
                employee.getAge(),
                employee.getDepartment().getName()
        );
    }
}
