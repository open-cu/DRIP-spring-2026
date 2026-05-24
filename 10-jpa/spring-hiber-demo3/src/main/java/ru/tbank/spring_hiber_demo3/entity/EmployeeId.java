package ru.tbank.spring_hiber_demo3.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

//@Embeddable
public class EmployeeId implements Serializable {

    private String departmentId;
    private Long employeeNumber;
}
