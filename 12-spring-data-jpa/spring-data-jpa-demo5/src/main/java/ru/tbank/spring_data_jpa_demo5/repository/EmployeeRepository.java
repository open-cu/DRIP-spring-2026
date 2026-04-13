package ru.tbank.spring_data_jpa_demo5.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tbank.spring_data_jpa_demo5.model.Employee;
import ru.tbank.spring_data_jpa_demo5.projection.EmployeeProjection;
import ru.tbank.spring_data_jpa_demo5.projection.EmployeeWithDepartmentSummaryProjection;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<EmployeeWithDepartmentSummaryProjection> findAllBy(Pageable pageable);

    @Query("""
            SELECT new ru.tbank.spring_data_jpa_demo5.projection.EmployeeProjection(e.name, e.salary)
            FROM Employee e
            WHERE e.department.name = :departmentName
            """)
    List<EmployeeProjection> findByDepartmentName(@Param("departmentName") String departmentName);

    <T> List<T> findByDepartmentName(String departmentName, Class<T> type);
}
