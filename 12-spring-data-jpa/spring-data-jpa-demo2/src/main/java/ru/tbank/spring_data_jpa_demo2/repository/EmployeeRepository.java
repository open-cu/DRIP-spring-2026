package ru.tbank.spring_data_jpa_demo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tbank.spring_data_jpa_demo2.model.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Основные префиксы методов
    boolean existsByName(String name);

    long countByAge(Integer age);

    void deleteByName(String name);

    List<Employee> findByName(String name);

    List<Employee> findByAge(Integer age);

    // Ключевые слова для условий
    List<Employee> findByNameAndAge(String name, Integer age);

    List<Employee> findByAgeBetween(Integer from, Integer to);

    List<Employee> findByNameIgnoreCase(String name);

    List<Employee> findByAgeOrderByNameAsc(Integer age);
}
