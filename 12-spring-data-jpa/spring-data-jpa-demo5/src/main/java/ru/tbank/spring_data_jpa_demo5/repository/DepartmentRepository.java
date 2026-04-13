package ru.tbank.spring_data_jpa_demo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tbank.spring_data_jpa_demo5.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
