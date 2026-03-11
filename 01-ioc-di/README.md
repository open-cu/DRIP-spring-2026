# Сквозной пример: мини-CRUD “Категории банковских операций”

Один и тот же проект проходит через несколько этапов эволюции управления зависимостями.  
Домен неизменен: CRUD категорий (Еда / Транспорт / Подписки).

Типовая цепочка слоёв:

CategoryController (консоль) → CategoryService → CategoryRepository → Storage

Точки замены (стратегии):
- CategoryNameValidator — Strict / Lenient
- Storage — InMemory / Jdbc

---

## step-00 — anti-DI

Каждый класс сам создаёт зависимости через `new`.

- Service создаёт Repository и Validator
- Repository создаёт Storage
- Всё жёстко прибито к конкретным реализациям

Проблемы:
- Нельзя легко заменить Storage или Validator
- Тесты становятся интеграционными
- Логика сборки размазана по всему коду

---

## step-01 — DIP + DI

Вводим интерфейсы:
- CategoryRepository
- Storage
- CategoryNameValidator
- CategoryService

Используем constructor injection.

Классы больше ничего не создают — только получают зависимости извне.

Сборка графа объектов переносится в `main`.

Плюсы:
- Реализации легко заменяются
- Зависимости явные
- Тестируемость резко упрощается

---

## step-02 — tests

Появляются настоящие юнит-тесты.

Подменяем зависимости:
- FakeCategoryRepository
- Stub/Spy CategoryService
- LenientValidator

Тестируем CategoryService без БД.

DI + DIP → изоляция бизнес-логики.
