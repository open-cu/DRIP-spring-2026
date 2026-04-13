package ru.tbank.spring_data_jpa_demo8.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tbank.spring_data_jpa_demo8.model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @EntityGraph(attributePaths = "department")
    Optional<Employee> findWithDepartmentById(Long id);

    List<Employee> findAllBy();

    @EntityGraph(attributePaths = "department")
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllWithDepartment();
}
