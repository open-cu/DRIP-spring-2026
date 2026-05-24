package ru.tbank.spring_hiber_demo2.dto;


public class Employee {

    private final Long id;
    private final String name;
    private final Integer age;
    private EmployeeProfile profile;

    public Employee(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public void setProfile(EmployeeProfile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", profile=" + profile +
                '}';
    }
}
