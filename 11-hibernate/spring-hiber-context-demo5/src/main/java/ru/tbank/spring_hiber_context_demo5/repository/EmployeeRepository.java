package ru.tbank.spring_hiber_context_demo5.repository;

import ru.tbank.spring_hiber_context_demo5.dto.EmployeeWithDepartment;
import ru.tbank.spring_hiber_context_demo5.model.Employee;

import java.util.Optional;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    Employee updateEmployeeName(Long id, String newName);

    void deleteById(Long id);

    // для примеров JPQL
    EmployeeWithDepartment getEmployeeWithDepartmentNameDto(Long id);
}
