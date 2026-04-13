package ru.tbank.spring_hiber_context_demo1.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import ru.tbank.spring_hiber_context_demo1.model.Employee;

@Repository
@RequiredArgsConstructor
public class EntityManagerDemoRepository {

    /**
     * Обратите внимание:
     * 1. @PersistenceContext здесь работает как метка
     *    Технически Spring может внедрить EntityManager через конструктор и без неё
     *    (так как это обычный бин в Spring Boot).
     * 2. Мы оставляем аннотацию намеренно, чтобы:
     *    - Явно показать, что это JPA-компонент (стандарт, а не только Spring)
     *    - Сделать код понятнее для других разработчиков
     * 3. Поле final — это правильно! Lombok создаст конструктор,
     *    и Spring внедрит зависимость через него (конструкторная инъекция).
     */
    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * ВНИМАНИЕ: В реальных приложениях @Transactional должна быть на Service слое!
     * Здесь она используется на Repository только для упрощения учебного примера,
     * чтобы не создавать лишние классы-прослойки.
     * Не копируйте этот паттерн в домашки и production-код.
     */
    // 1. persist() — New → Managed
    @Transactional
    public void demonstratePersist() {
        System.out.println("1. persist() — New → Managed");

        Employee emp = new Employee("Anton", 23);
        System.out.println("До persist: " + emp); // id = null
        System.out.println("Отслеживается? " + entityManager.contains(emp)); // false

        entityManager.persist(emp);
        System.out.println("После persist: " + emp); // id всё ещё null (до flush)
        System.out.println("Отслеживается? " + entityManager.contains(emp)); // true

        System.out.println("--> Состояние: New → Managed\n");
    }

    // 2. find() — получение Managed-сущности
    @Transactional
    public void demonstrateFind() {
        System.out.println("2. find() — получение Managed-сущности");

        Employee emp = entityManager.find(Employee.class, 1L);
        System.out.println("Найден: " + emp);
        System.out.println("Отслеживается? " + entityManager.contains(emp)); // true

        System.out.println("--> Состояние: → Managed\n");
    }

    // 3. detach() и merge() — Managed → Detached → Managed
    @Transactional
    public void demonstrateDetachAndMerge() {
        System.out.println("3. detach() и merge() — Managed → Detached → Managed");

        Employee emp = entityManager.find(Employee.class, 1L);
        System.out.println("До detach: " + entityManager.contains(emp)); // true

        entityManager.detach(emp);
        System.out.println("После detach: " + entityManager.contains(emp)); // false

        // Меняем данные (вне контекста)
        emp.setAge(31);

        // merge() — возвращает Managed-копию
        Employee merged = entityManager.merge(emp);
        System.out.println("После merge: " + entityManager.contains(merged)); // true
        System.out.println("Объект merged: " + merged);
        System.out.println("Оригинал emp (detached): " + emp);

        System.out.println("--> Состояние: Managed → Detached → Managed\n");
    }

    // 4. remove() — Managed → Removed
    @Transactional
    public void demonstrateRemove() {
        System.out.println("4. remove() — Managed → Removed");

        Employee emp = entityManager.find(Employee.class, 1L);
        System.out.println("Найден для удаления: " + emp);

        entityManager.remove(emp);
        entityManager.setFlushMode(FlushModeType.COMMIT);

        System.out.println("После remove: " + entityManager.contains(emp)); // false

        System.out.println("--> Состояние: Managed → Removed\n");
    }

    // 5. refresh() — перезагрузка из БД
    @Transactional
    public void demonstrateRefresh() {
        System.out.println("5. refresh() — синхронизация с БД");

        Employee emp = new Employee("Dima", 28);
        entityManager.persist(emp);

        // Меняем в памяти
        emp.setAge(999);
        System.out.println("До refresh: " + emp); // age = 999

        entityManager.refresh(emp); // перечитывает из БД
        System.out.println("После refresh: " + emp); // age = 28

        System.out.println("--> refresh() отменяет локальные изменения\n");
    }
}
