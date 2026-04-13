package ru.tbank.spring_hiber_demo1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tbank.spring_hiber_demo1.dto.Employee;
import ru.tbank.spring_hiber_demo1.dto.EmployeeProfile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringHiberDemo1Application implements CommandLineRunner {

    // H2 in-memory с режимом PostgreSQL, чтобы поддержать BIGSERIAL/TEXT
    private static final String URL = "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) {
        SpringApplication.run(SpringHiberDemo1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1) INSERT с возвратом сгенерированного id
        long id;
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO employee(name, age) VALUES(?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Anton");
                ps.setInt(2, 23);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) { throw new RuntimeException("No ID returned"); }
                    id = keys.getLong(1);
                }
            }
        }
        System.out.println("employee id = " + id);

        // 2) SELECT по id
        long employeeId = 1L;
        Employee employee = null;
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT id, name, age FROM employee WHERE id = ?")) {
                ps.setLong(1, employeeId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        employee = new Employee(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getInt("age")
                        );
                    }
                }
            }
        }
        System.out.println("selected employee = " + employee);

        // 2) SELECT всех
        List<Employee> all = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, name, age FROM employee")) {
                while (rs.next()) {
                    all.add(new Employee(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getInt("age")));
                }
            }
        }
        System.out.println("all employees = " + all);

        // 3) UPDATE
        int countUpdatedEmployees;
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE employee SET name = ?, age = ? WHERE id = ?")) {
                ps.setString(1, "Anton Updated");
                ps.setInt(2, 31);
                ps.setLong(3, 1);
                countUpdatedEmployees = ps.executeUpdate();
            }
        }
        System.out.println("Count updated employees = " + countUpdatedEmployees);

        // 4) INSERT профиля с возвратом сгенерированного id
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO employee_profile(phone, emergency_contact, employee_id) VALUES(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "+79256966869");
                ps.setString(2, "+73456966079");
                ps.setInt(3, 1);
                ps.executeUpdate();
            }
        }

        // 4) SELECT с JOIN: получить сотрудника и его профиль
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            Employee employeeWithProfile = null;

            String sql = """
                    SELECT e.id, e.name, e.age, ep.id AS profile_id, ep.phone, ep.emergency_contact, ep.employee_id
                    FROM employee e
                    LEFT JOIN employee_profile ep ON e.id = ep.employee_id
                    WHERE e.id = ?
                    """;

            id = 1L;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        employeeWithProfile = new Employee(rs.getLong("id"), rs.getString("name"),
                                rs.getInt("age")
                        );
                        // Проверяем, есть ли профиль (вдруг LEFT JOIN вернул NULL)
                        Long profileId = rs.getObject("profile_id", Long.class);
                        if (profileId != null) {
                            EmployeeProfile profile = new EmployeeProfile(profileId, rs.getString("phone"),
                                    rs.getString("emergency_contact"), rs.getLong("employee_id")
                            );
                            employeeWithProfile.setProfile(profile);
                        }
                    }
                }
            }
            System.out.println("Joined result: " + employeeWithProfile);
        }

        // 5) DELETE
        int countDeletedEmployeeProfiles;
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM employee_profile WHERE id = ?")) {
                ps.setLong(1, 1);
                countDeletedEmployeeProfiles = ps.executeUpdate();
            }
        }
        System.out.println("Count deleted employee profiles = " + countDeletedEmployeeProfiles);

        // 5) DELETE
        int countDeletedEmployees;
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM employee WHERE id = ?")) {
                ps.setLong(1, 1);
                countDeletedEmployees = ps.executeUpdate();
            }
        }
        System.out.println("Count deleted employees = " + countDeletedEmployees);
    }
}
