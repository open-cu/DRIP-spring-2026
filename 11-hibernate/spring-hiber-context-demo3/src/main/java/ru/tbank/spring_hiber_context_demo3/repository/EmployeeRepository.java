package ru.tbank.spring_hiber_context_demo3.repository;

import ru.tbank.spring_hiber_context_demo3.model.Employee;

import java.util.Optional;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    void deleteById(Long id);
}
