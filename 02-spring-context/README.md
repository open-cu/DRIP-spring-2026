# Сквозной пример: мини-CRUD “Категории банковских операций”

Один и тот же проект проходит через несколько этапов эволюции управления зависимостями.  
Домен неизменен: CRUD категорий (Еда / Транспорт / Подписки).

Типовая цепочка слоёв:

CategoryController (консоль) → CategoryService → CategoryRepository → Storage

Точки замены (стратегии):
- CategoryNameValidator — Strict / Lenient
- Storage — InMemory / Jdbc

---

## step-03 — Spring XML

Сборка графа переносится в `application-context.xml`.

Контейнер:
- создаёт бины
- связывает зависимости
- управляет singleton’ами

`main`:
запустить Spring Context → получить CategoryController → run()

---

## step-04 — Spring Java Config

XML заменяется на `@Configuration` + `@Bean`.

Преимущества:
- Типобезопасность
- Удобство рефакторинга
- Конфигурация на Java

---

## step-05 — Spring Annotations

Компоненты помечены:
- `@Component`
- `@Service`
- `@Repository`

Включён `@ComponentScan`.

Контейнер сам:
- находит классы
- строит граф зависимостей
- управляет жизненным циклом

Выбор реализаций (Strict vs Lenient, InMemory vs Jdbc) уходит в конфигурацию или профили.

---

# Итог

Один и тот же CRUD-проект показывает эволюцию:

anti-DI → DIP + DI → tests → Spring (XML → JavaConfig → annotations)

Главная идея:
бизнес-код не должен создавать зависимости —
он должен их получать.
Контейнер автоматизирует сборку и управление жизненным циклом.
