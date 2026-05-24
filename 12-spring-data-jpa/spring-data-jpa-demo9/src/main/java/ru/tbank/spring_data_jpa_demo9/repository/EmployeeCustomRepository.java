package ru.tbank.spring_data_jpa_demo9.repository;

import ru.tbank.spring_data_jpa_demo9.model.Employee;

import java.util.List;

public interface EmployeeCustomRepository {

    List<Employee> findEmployeesByFilters(String name, Integer minAge, String departmentName);

}
