package ru.tbank.spring_hiber_demo1.dto;

@SuppressWarnings("all")
public class EmployeeProfile {

    private Long id;
    private String phone;
    private String emergencyContact;
    private Long employeeId;

    public EmployeeProfile(Long id, String phone,
                           String emergencyContact, Long employeeId) {
        this.id = id;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "EmployeeProfile{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", employeeId=" + employeeId +
                '}';
    }
}
