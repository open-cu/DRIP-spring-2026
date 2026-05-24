package ru.tbank.spring_hiber_context_demo5.repository;

import ru.tbank.spring_hiber_context_demo5.model.Department;

import java.util.List;

public interface DepartmentRepository {

    Department save(Department department);

    List<Department> findAll();

    Department findById(Long id);

    List<Department> findDepartmentsWithEmployees(int page, int size);
}
