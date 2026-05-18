# 02-resttemplate-client

CLI-приложение, которое вызывает `01-provider-api-openapi` через `RestTemplate`.

## Роль в лекции

Этот пример нужен как базовая синхронная реализация HTTP client-а. `RestTemplate`
часто встречается в существующих Spring-проектах, поэтому важно уметь читать такой
код, поддерживать его и понимать, где в нем настраиваются интеграционные правила.

Главная мысль: `RestTemplate` не является "плохим" сам по себе, но для нового
императивного кода в современном Spring обычно удобнее начинать с `RestClient`.

## Что изучается

- Blocking HTTP-вызовы: текущий поток ждет ответа provider-а.
- `RestTemplateBuilder` вместо ручного `new RestTemplate()`.
- `rootUri`, connect timeout, read timeout.
- `ResponseErrorHandler` как центральное место обработки `4xx/5xx`.
- `getForObject`, `postForObject`, `exchange`.
- `ParameterizedTypeReference<List<CourseDto>>` для generic response.
- Передача JSON request body и чтение JSON response body.

## Какие файлы открыть

- `src/main/java/edu/course/resttemplate/config/RestTemplateConfig.java` - общий
  builder: base URL, timeouts, error handler.
- `src/main/java/edu/course/resttemplate/http/CourseApiErrorHandler.java` -
  превращает HTTP error response в `CourseApiException`.
- `src/main/java/edu/course/resttemplate/DemoRunner.java` - последовательность
  учебных вызовов: list, get by id, post enrollment, missing course.
- `src/main/java/edu/course/resttemplate/dto/*.java` - локальные DTO клиента,
  совпадающие с provider contract.
- `src/main/resources/application.yml` - default base URL для локального запуска.

## Запуск через Docker Compose

Из корня папки `spring-integration-restclients-openapi` сначала запустите provider:

```bash
docker compose up --build provider-api
```

В соседнем терминале:

```bash
docker compose run --rm resttemplate-client
```

## Что должно быть видно

В консоли клиента должны появиться:

- список курсов из `GET /api/courses`;
- один курс из `GET /api/courses/{id}`;
- результат записи на курс из `POST /api/enrollments`;
- обработанная ошибка `404` для `missing-course`.

Если provider не запущен, пример должен завершиться ошибкой подключения. Это
нормально: так видно, что HTTP client зависит от сетевой доступности другого сервиса.

## Связь с лекцией

- Глава про `RestTemplate`: показывает template-style API и централизованную
  конфигурацию.
- Глава про timeouts: значения заданы в `RestTemplateConfig`.
- Глава про ошибки: `CourseApiErrorHandler` сохраняет status code и body ответа.
- Глава про generic responses: список курсов читается через
  `ParameterizedTypeReference`.

## Вопросы студентам

- Почему `RestTemplateBuilder` лучше, чем `new RestTemplate()` в каждом классе?
- Что потеряется, если в `CourseApiErrorHandler` не читать тело ошибки?
- Почему для `List<CourseDto>` недостаточно просто передать `List.class`?
- Что изменится, если `404` нужно маппить не в exception, а в `Optional.empty()`?

## Практические изменения

- Уменьшить read timeout и посмотреть, как клиент ведет себя при медленном provider-е.
- Добавить метод `findCourse(String id)`, который возвращает `Optional<CourseDto>`.
- Добавить header `X-Request-Id` через builder/interceptor и сравнить с
  `RestClient`/`WebClient`.
