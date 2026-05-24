package ru.tbank.spring_data_jpa_demo3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tbank.spring_data_jpa_demo3.model.Employee;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.age > 18
            ORDER BY e.age ASC
            """)
    List<Employee> findAllOrderedByAgeAsc();

    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.department.name = :departmentName
            AND e.salary > :salary
            """)
    List<Employee> findByDepartmentAndSalary(
            @Param("departmentName") String departmentName,
            @Param("salary") BigDecimal salary
    );

    @Query(value = """
        SELECT e.id, e.name, e.age, e.salary, e.department_id
        FROM employees e
        JOIN departments d ON d.id = e.department_id
        WHERE d.name = :departmentName
        AND e.salary > :salary
        """, nativeQuery = true)
    List<Employee> findByDepartmentAndSalaryNativeSQL(
            @Param("departmentName") String departmentName,
            @Param("salary") BigDecimal salary
    );

    // ВАЖНО! Будьте аккуратны с использованием параметров clearAutomatically и
    // flushAutomatically в продакшене, так как они влияют на производительность!
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE Employee e
            SET e.salary = :salary
            WHERE e.name = :name
            """)
    int updateSalaryByName(
            @Param("name") String name,
            @Param("salary") BigDecimal salary
    );
}
