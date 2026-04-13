package ru.tbank.spring_data_jpa_demo8.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tbank.spring_data_jpa_demo8.model.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(
            value = """
                    select d
                    from Department d
                    left join fetch d.employees
                    """,
            countQuery = "select count(d) from Department d"
    )
    Page<Department> findAllWithEmployeesFetchJoin(Pageable pageable);

    Page<Department> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = "employees")
    List<Department> findByIdIn(List<Long> ids);
}
