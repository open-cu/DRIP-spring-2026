package ru.tbank.spring_hiber_context_demo5.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class EmployeeWithDepartment {

    private String name;
    private Integer age;
    private String departmentName;
}
