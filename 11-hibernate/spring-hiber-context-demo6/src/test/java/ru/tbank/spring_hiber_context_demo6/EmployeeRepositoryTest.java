package ru.tbank.spring_hiber_context_demo6;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.tbank.spring_hiber_context_demo6.model.Employee;
import ru.tbank.spring_hiber_context_demo6.repository.EmployeeRepositoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA репозиторий для работы с сотрудниками")
@DataJpaTest
@Import(EmployeeRepositoryImpl.class)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepositoryImpl employeeRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Проверка загрузки сотрудника по id")
    @Test
    void testFindEmployeeById() {
        // given
        long FIRST_EMPLOYEE_ID = 1L;

        // when
        Employee employeeFromRepository = employeeRepository.findById(FIRST_EMPLOYEE_ID).get();
        Employee employeeFromEntityManager = testEntityManager.find(Employee.class, FIRST_EMPLOYEE_ID);

        // then
        System.out.println("employeeFromRepository = " + employeeFromRepository);
        System.out.println("employeeFromEntityManager = " + employeeFromEntityManager);
        assertThat(employeeFromRepository).usingRecursiveComparison().isEqualTo(employeeFromEntityManager);
    }


    @DisplayName("Проверка загрузки сотрудников с дочерними сущностями")
    @Test
    void testFindAllEmployees() {
        // given
        int EXPECTED_QUERIES_COUNT = 1;
        int EXPECTED_NUMBER_OF_EMPLOYEES = 5;
        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        // when
        List<Employee> all = employeeRepository.findAll();

        assertThat(all).isNotNull()
                .hasSize(EXPECTED_NUMBER_OF_EMPLOYEES)
                .allMatch(e -> e.getEmployeeProfile() != null)
                .allMatch(e -> e.getDepartment() != null);

        // then
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }
}
