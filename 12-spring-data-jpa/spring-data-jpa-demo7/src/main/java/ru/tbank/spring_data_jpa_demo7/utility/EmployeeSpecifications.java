package ru.tbank.spring_data_jpa_demo7.utility;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.tbank.spring_data_jpa_demo7.model.Employee;

import java.math.BigDecimal;

public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static Specification<Employee> hasName(String name) {
        return (Root<Employee> root,
                CriteriaQuery<?> query,
                CriteriaBuilder cb) -> cb.equal(root.get("name"), name);
    }

    public static Specification<Employee> ageGreaterThan(Integer age) {
        return (Root<Employee> root,
                CriteriaQuery<?> query,
                CriteriaBuilder cb) -> cb.greaterThan(root.get("age"), age);
    }

    public static Specification<Employee> salaryGreaterThan(BigDecimal salary) {
        return (Root<Employee> root,
                CriteriaQuery<?> query,
                CriteriaBuilder cb) -> cb.greaterThan(root.get("salary"), salary);
    }

    public static Specification<Employee> departmentNameEquals(String departmentName) {
        return (Root<Employee> root,
                CriteriaQuery<?> query,
                CriteriaBuilder cb) ->
                cb.equal(root.get("department").get("name"), departmentName);
    }

    public static Specification<Employee> nameContainsIgnoreCase(String value) {
        return (Root<Employee> root,
                CriteriaQuery<?> query,
                CriteriaBuilder cb) ->
                cb.like(cb.lower(root.get("name")), "%" + value.toLowerCase() + "%");
    }
}
