package ru.tbank.spring_hiber_demo5.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee_profiles")
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "emergency_contact")
    private String emergencyContact;

//    @OneToOne(fetch = FetchType.LAZY)
    @OneToOne(mappedBy = "employeeProfile", fetch = FetchType.LAZY)
    private Employee employee;
}
