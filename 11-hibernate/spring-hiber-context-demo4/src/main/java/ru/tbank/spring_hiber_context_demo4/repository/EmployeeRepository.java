package ru.tbank.spring_hiber_context_demo4.repository;

import jakarta.persistence.Tuple;
import ru.tbank.spring_hiber_context_demo4.dto.EmployeeAge;
import ru.tbank.spring_hiber_context_demo4.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    Employee updateEmployeeName(Long id, String newName);

    void deleteById(Long id);

    // для примеров JPQL
    List<EmployeeAge> getEmployeeAge();

    List<Tuple> findEmployeeStatsByDepartmentIdAsTuple(Long deptId);

    List<Tuple> findDepartmentStatsWithHighAvgAgeAsTuple();

    List<Employee> findEmployeesWithNoProjects();

    List<Employee> findEmployeesWithDepartmentNameLike();

    List<Employee> findEmployeesWithAgeAboveAverageInDepartment();

    List<Employee> findEmployeesWithAgeGreaterThanAnyInHR();

    List<Employee> findEmployeesWithAgeGreaterThanAllInHR();

    List<Employee> findByName(String name);

    void updateAgeStale(Long id, Integer age);

    void deleteEmployeeByNameStale(String name);

    void demoStaleStateUpdate(Long employeeId);

    void demoStaleStateDelete(Long employeeId);
}
