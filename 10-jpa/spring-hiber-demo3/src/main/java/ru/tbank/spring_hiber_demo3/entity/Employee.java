package ru.tbank.spring_hiber_demo3.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    private Long id;
//
//    @EmbeddedId
//    private EmployeeId id;
}
