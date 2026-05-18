# 03-restclient-client

CLI-приложение, которое вызывает `01-provider-api-openapi` через `RestClient`.

## Роль в лекции

Этот пример показывает современный синхронный HTTP client в Spring. Он остается
blocking, как `RestTemplate`, но дает fluent API, похожий по форме на `WebClient`.

Главная мысль: если приложение обычное Spring MVC/imperative и нет задачи жить в
Reactor-модели, `RestClient` часто является самым прямым выбором для нового кода.

## Что изучается

- Blocking HTTP-вызовы с fluent API.
- `RestClient.Builder` и общая конфигурация provider-а.
- `defaultHeader` и `defaultStatusHandler`.
- GET одного объекта и списка объектов.
- POST JSON body.
- Generic body через `ParameterizedTypeReference`.
- Единый exception type для HTTP errors.

## Какие файлы открыть

- `src/main/java/edu/course/restclient/config/RestClientConfig.java` - base URL,
  default header и default status handler.
- `src/main/java/edu/course/restclient/DemoRunner.java` - GET/POST-вызовы в fluent
  стиле.
- `src/main/java/edu/course/restclient/http/CourseApiException.java` - простая
  ошибка integration layer.
- `src/main/java/edu/course/restclient/dto/*.java` - DTO, соответствующие provider
  contract.
- `src/main/resources/application.yml` - default base URL.

## Запуск через Docker Compose

Из корня папки `spring-integration-restclients-openapi` сначала запустите provider:

```bash
docker compose up --build provider-api
```

В соседнем терминале:

```bash
docker compose run --rm restclient-client
```

## Что должно быть видно

В выводе будут те же сценарии, что и в `02-resttemplate-client`:

- список курсов;
- получение одного курса;
- запись на курс;
- обработка `404`.

Одинаковые сценарии специально повторяются, чтобы сравнивать не бизнес-логику, а
форму клиентского кода.

## Связь с лекцией

- Глава про `RestClient`: показывает рекомендуемый стиль для нового
  синхронного Spring-кода.
- Глава про adapter layer: `RestClientConfig` задает техническую политику один раз.
- Глава про сравнение клиентов: этот пример стоит смотреть сразу после
  `02-resttemplate-client`.
- Глава про OpenAPI generation: generated REST client обычно тоже прячут за таким
  же adapter-слоем.

## Вопросы студентам

- Чем `retrieve().body(...)` читается проще или сложнее, чем `getForObject`?
- Где лучше задавать auth/header: в каждом вызове или в builder-е?
- Почему `RestClient` не делает код reactive, несмотря на похожий fluent style?
- Как бы вы спрятали этот клиент за интерфейсом `CourseProviderClient`?

## Практические изменения

- Добавить метод `createEnrollment(...)` в отдельный adapter class.
- Добавить per-request header и обсудить, чем он отличается от `defaultHeader`.
- Изменить обработку `404` так, чтобы конкретный метод возвращал `Optional`.
