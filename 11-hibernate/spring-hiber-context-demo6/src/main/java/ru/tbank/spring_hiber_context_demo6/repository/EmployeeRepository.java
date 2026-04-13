package ru.tbank.spring_hiber_context_demo6.repository;

import ru.tbank.spring_hiber_context_demo6.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    List<Employee> findAll();

    Employee updateEmployeeName(Long id, String newName);

    void deleteById(Long id);
}
