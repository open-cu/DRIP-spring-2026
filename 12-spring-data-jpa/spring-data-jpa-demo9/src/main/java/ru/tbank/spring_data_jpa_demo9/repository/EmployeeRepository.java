package ru.tbank.spring_data_jpa_demo9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tbank.spring_data_jpa_demo9.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeCustomRepository {
}
