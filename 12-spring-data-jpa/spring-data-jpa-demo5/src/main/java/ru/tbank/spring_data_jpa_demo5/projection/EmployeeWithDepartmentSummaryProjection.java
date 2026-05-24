package ru.tbank.spring_data_jpa_demo5.projection;

import org.springframework.beans.factory.annotation.Value;

// Важно! Названия полей проекции должны быть идентичны названиям полей entity, иначе магии не будет
public interface EmployeeWithDepartmentSummaryProjection {

    String getName();

    Integer getAge();

    DepartmentSummary getDepartment();

    interface DepartmentSummary {
        String getName();
    }

    // Interface-based проекции – «закрытые», то есть их атрибуты должны точно
    // соответствовать сущности, которую они проецируют.
    // 1. Мы можем вычислять значения полей самостоятельно через дефолтные методы
    default String getNameWithDepartmentName() {
        return getName().concat(" ").concat(getDepartment().getName());
    }

    // 2. Мы можем вычислять значения полей самостоятельно через SpEL (Spring Expression Language)
    @Value("#{target.name + ' ' + target.age}")
    String getNameWthAge();

    // 3. Мы можем написать кастомные бины для сложной логики
    // @Component
    // class MyBean {
    //  String getNameWthAge(Employee employee) { *сложная логика* }
    // }
    //
    // interface EmployeeWithDepartmentSummaryProjection {
    //  @Value("#{@myBean.getNameWthAge(target)}")
    //  String getNameWthAge();
    // }
}
