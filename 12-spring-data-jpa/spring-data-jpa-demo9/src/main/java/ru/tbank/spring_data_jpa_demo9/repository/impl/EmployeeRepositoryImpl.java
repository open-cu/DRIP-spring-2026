package ru.tbank.spring_data_jpa_demo9.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import ru.tbank.spring_data_jpa_demo9.model.Employee;
import ru.tbank.spring_data_jpa_demo9.repository.EmployeeCustomRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeCustomRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Employee> findEmployeesByFilters(String name, Integer minAge, String departmentName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("name")),
                            "%" + name.toLowerCase() + "%"
                    )
            );
        }

        if (minAge != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), minAge));
        }

        if (departmentName != null && !departmentName.isBlank()) {
            predicates.add(
                    cb.equal(
                            root.get("department").get("name"),
                            departmentName
                    )
            );
        }

        cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(cq).getResultList();
    }
}
