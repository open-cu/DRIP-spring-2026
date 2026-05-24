# 01-provider-api-openapi

Учебный REST API provider, вокруг которого построены все HTTP-примеры лекции.
Этот сервис изображает внешнюю образовательную платформу: у нее есть каталог курсов,
endpoint записи на курс, поток событий и OpenAPI-документация.

## Роль в лекции

Этот пример открывает тему интеграции с другими приложениями. На нем удобно показать,
что интеграция начинается не с выбора клиента (`RestTemplate`, `RestClient` или
`WebClient`), а с границы между сервисами:

- какие URL и HTTP methods доступны;
- какие DTO уходят наружу;
- какие HTTP statuses получает клиент;
- какой формат ошибки считается частью контракта;
- как контракт можно опубликовать через OpenAPI.

## Что изучается

- Spring MVC `@RestController` как provider-side API.
- DTO на Java records как внешний JSON-контракт.
- Bean Validation для проверки входящего запроса.
- `ProblemDetail` и `@RestControllerAdvice` для единообразных ошибок.
- HTTP statuses `200`, `201`, `400`, `404`.
- SSE endpoint (`text/event-stream`) для демонстрации streaming-сценария.
- springdoc-openapi и Swagger UI для публикации машинно-читаемого контракта.

## Какие файлы открыть

- `src/main/java/edu/course/provider/course/CourseController.java` - каталог курсов:
  `GET /api/courses` и `GET /api/courses/{id}`.
- `src/main/java/edu/course/provider/enrollment/EnrollmentController.java` - запись
  на курс: `POST /api/enrollments`, `201 Created`, validation.
- `src/main/java/edu/course/provider/error/GlobalErrorHandler.java` - mapping
  исключений в `ProblemDetail`.
- `src/main/java/edu/course/provider/notification/NotificationController.java` -
  короткий SSE stream для примера на `WebClient`.
- `src/main/java/edu/course/provider/course/CourseDto.java` и
  `src/main/java/edu/course/provider/enrollment/EnrollmentRequest.java` - DTO,
  которые затем повторяются в клиентских примерах.
- `src/main/resources/application.yml` - порт сервиса и настройки springdoc.

## Запуск через Docker Compose

Из корня папки `spring-integration-restclients-openapi`:

```bash
docker compose up --build provider-api
```

API будет доступен на `http://localhost:8081`.

Проверочные URL в браузере или через `curl`:

- `http://localhost:8081/api/courses`
- `http://localhost:8081/api/courses/spring-rest`
- `http://localhost:8081/api/notifications`
- `http://localhost:8081/swagger-ui.html`
- `http://localhost:8081/v3/api-docs`

## Что должно быть видно

- `/api/courses` возвращает JSON-массив курсов.
- `/api/courses/spring-rest` возвращает один курс.
- `/api/courses/missing-course` возвращает `404` и тело в формате `ProblemDetail`.
- `/api/enrollments` с невалидным email возвращает `400` и список ошибок validation.
- `/api/notifications` отдает несколько событий в формате SSE.
- `/v3/api-docs` содержит OpenAPI JSON, который читает пример
  `05-openapi-contract-inspector`.

## Связь с лекцией

- Глава про provider API и контракт: показывает, что path, method, DTO, status и
  error body являются частью интеграционного договора.
- Глава про `RestTemplate`, `RestClient`, `WebClient`: все три клиента вызывают
  именно этот сервис, чтобы сравнение было честным.
- Глава про OpenAPI: springdoc строит OpenAPI-документ из контроллеров и DTO.
- Глава про streaming: SSE endpoint нужен, чтобы увидеть отличие `Flux` от обычного
  blocking-вызова.

## Вопросы студентам

- Что сломается у клиента, если переименовать поле `durationHours`?
- Почему `404` лучше явно описывать, а не оставлять как случайную ошибку?
- Где заканчивается внутренняя модель сервиса и начинается внешний DTO-контракт?
- Почему Swagger UI не заменяет сам OpenAPI-документ?

## Практические изменения

- Добавить query parameter `status` в `GET /api/courses` и посмотреть, как меняется
  OpenAPI.
- Добавить новый validation rule в `EnrollmentRequest` и проверить `400 ProblemDetail`.
- Добавить новый event type в `NotificationController` и прочитать его из
  `04-webclient-client`.
