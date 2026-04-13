package ru.tbank.spring_hiber_context_demo2.model;

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

    @OneToOne(fetch = FetchType.LAZY)
    private Employee employee;

    public EmployeeProfile(String phone, String emergencyContact) {
        this.phone = phone;
        this.emergencyContact = emergencyContact;
    }

    @Override
    public String toString() {
        return "EmployeeProfile{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                '}';
    }
}
