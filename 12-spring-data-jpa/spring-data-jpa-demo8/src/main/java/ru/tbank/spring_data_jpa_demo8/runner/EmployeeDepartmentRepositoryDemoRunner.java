package ru.tbank.spring_data_jpa_demo8.runner;

import lombok.RequiredArgsConstructor;
import org.hibernate.LazyInitializationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_data_jpa_demo8.model.Department;
import ru.tbank.spring_data_jpa_demo8.model.Employee;
import ru.tbank.spring_data_jpa_demo8.repository.DepartmentRepository;
import ru.tbank.spring_data_jpa_demo8.repository.EmployeeRepository;
import ru.tbank.spring_data_jpa_demo8.service.JpaDemoProblemService;

import java.math.BigDecimal;
import java.util.List;

/**
 * ВНИМАНИЕ: УЧЕБНЫЙ ПРИМЕР (DEMO ONLY)
 * <p>
 * Этот класс используется ТОЛЬКО для автоматического запуска демонстрации при старте приложения.
 * <p>
 * В РЕАЛЬНОМ ПРОЕКТЕ архитектура выглядит иначе:
 * <p>
 * HTTP Request
 * ↓
 * [Controller]  ← Принимает запрос
 * ↓
 * [Service]     ← @Transactional, бизнес-логика
 * ↓
 * [Repository]  ← Работа с EntityManager/БД
 * ↓
 * [Database]
 * <p>
 * Почему здесь иначе?
 * - Мы вызываем Repository напрямую, чтобы не создавать лишние классы-прослойки.
 * - Наша цель — показать работу Spring Data JPA, а не архитектуру веб-приложения.
 * <p>
 * НЕ КОПИРУЙТЕ этот паттерн в домашки и production-код!
 */
@Component
@RequiredArgsConstructor
public class EmployeeDepartmentRepositoryDemoRunner implements CommandLineRunner {

    private final JpaDemoProblemService jpaDemoProblemService;

    @Override
    public void run(String... args) throws Exception {
        jpaDemoProblemService.prepareData();

        jpaDemoProblemService.demoLazyInitializationException();
        jpaDemoProblemService.demoLazyInitializationExceptionSolved();
        jpaDemoProblemService.demoNPlusOne();
        jpaDemoProblemService.demoEntityGraph();
        jpaDemoProblemService.demoPaginationWithCollectionFetchJoin();
        jpaDemoProblemService.demoPaginationCorrect();
    }
}