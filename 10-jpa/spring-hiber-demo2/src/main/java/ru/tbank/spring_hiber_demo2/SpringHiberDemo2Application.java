package ru.tbank.spring_hiber_demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.tbank.spring_hiber_demo2.dto.Employee;
import ru.tbank.spring_hiber_demo2.dto.EmployeeProfile;

import java.util.Optional;

@SpringBootApplication
public class SpringHiberDemo2Application implements CommandLineRunner {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringHiberDemo2Application.class, args);
    }

    @Override
    public void run(String... args) {
        // 1) INSERT с возвратом сгенерированного id
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", "Anton");
        params.addValue("age", 23);

        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update("INSERT INTO employee(name, age) VALUES(:name, :age)",
                params, kh, new String[]{"id"});
        long id = kh.getKey().longValue();
        System.out.println("employee id = " + id);

        // 2) SELECT
        final RowMapper<Employee> EMPLOYEE_MAPPER = (rs, rowNum) ->
                new Employee(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("age")
                );

        MapSqlParameterSource selectParams = new MapSqlParameterSource();
        selectParams.addValue("id", 1L);

        Optional<Employee> employee = jdbcTemplate.query(
                "SELECT id, name, age FROM employee WHERE id = :id",
                        selectParams, EMPLOYEE_MAPPER)
                .stream()
                .findFirst();
        System.out.println("selected employee = " + employee.get());

        // 3) UPDATE
        String updateSQL = "UPDATE employee SET name = :name, age = :age WHERE id = :id";
        MapSqlParameterSource updateParams = new MapSqlParameterSource();
        updateParams.addValue("name", "Anton Updated");
        updateParams.addValue("age", 31);
        updateParams.addValue("id", 1L);

        int countUpdatedEmployees = jdbcTemplate.update(updateSQL, updateParams);
        System.out.println("Count updated employees = " + countUpdatedEmployees);

        // 4) INSERT профиля
        params = new MapSqlParameterSource();
        params.addValue("phone", "+79256966869");
        params.addValue("emergency_contact", "+73456966079");
        params.addValue("employee_id", 1);

        jdbcTemplate.update("INSERT INTO employee_profile(phone, emergency_contact, employee_id) " +
                "VALUES(:phone, :emergency_contact, :employee_id)", params);

        // 4) SELECT с JOIN: employee + employee_profile
        String sql = """
                SELECT e.id, e.name, e.age, ep.id AS profile_id, ep.phone, ep.emergency_contact, ep.employee_id
                FROM employee e
                LEFT JOIN employee_profile ep ON e.id = ep.employee_id
                WHERE e.id = :id
                """;

        MapSqlParameterSource joinParams = new MapSqlParameterSource();
        joinParams.addValue("id", 1L);

        Employee employeeWithProfile = jdbcTemplate.queryForObject(sql, joinParams, (rs, rowNum) -> {
            Employee emp = new Employee(rs.getLong("id"),
                    rs.getString("name"), rs.getInt("age")
            );

            Long profileId = rs.getObject("profile_id", Long.class);
            if (profileId != null) {
                EmployeeProfile profile = new EmployeeProfile(profileId, rs.getString("phone"),
                        rs.getString("emergency_contact"), rs.getLong("employee_id")
                );
                emp.setProfile(profile);
            }
            return emp;
        });

        System.out.println("employeeWithProfile = " + employeeWithProfile);

        // 5) DELETE
        MapSqlParameterSource deleteParams1 = new MapSqlParameterSource();
        deleteParams1.addValue("id", 1L);
        int countDeletedProfiles = jdbcTemplate.update("DELETE FROM employee_profile WHERE id = :id", deleteParams1);
        System.out.println("Count deleted profiles = " + countDeletedProfiles);

        MapSqlParameterSource deleteParams2 = new MapSqlParameterSource();
        deleteParams2.addValue("id", 1L);
        int countDeletedEmployees = jdbcTemplate.update("DELETE FROM employee WHERE id = :id", deleteParams2);
        System.out.println("Count deleted employees = " + countDeletedEmployees);
    }
}