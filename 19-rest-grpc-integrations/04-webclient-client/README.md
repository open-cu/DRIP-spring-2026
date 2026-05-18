# 04-webclient-client

CLI-приложение, которое вызывает `01-provider-api-openapi` через `WebClient`.

## Роль в лекции

Этот пример показывает reactive HTTP client. Он нужен не потому, что "новее", а
потому что у него другая модель выполнения: запрос описывается как `Mono` или
`Flux`, а выполняется при подписке.

Главная мысль: `WebClient` особенно уместен для WebFlux, композиции независимых
асинхронных вызовов и streaming response. Если в обычном MVC-коде после каждого
вызова сразу писать `.block()`, выгода reactive-подхода почти исчезает.

## Что изучается

- `WebClient.Builder` и общий base URL.
- `ExchangeFilterFunction` для сквозного logging/tracing.
- `Mono<T>` как описание одного будущего значения.
- `Flux<T>` как описание потока значений.
- `onStatus(...)` и преобразование error body в exception.
- `Mono.zip(...)` для композиции независимых запросов.
- SSE endpoint и чтение stream через `bodyToFlux(...)`.
- `.block(...)` как граница между reactive pipeline и учебным CLI-приложением.

## Какие файлы открыть

- `src/main/java/edu/course/webclient/config/WebClientConfig.java` - base URL,
  default header и filter.
- `src/main/java/edu/course/webclient/DemoRunner.java` - `Mono`, `Flux`,
  `onStatus`, `Mono.zip`, SSE.
- `src/main/java/edu/course/webclient/http/CourseApiException.java` - exception,
  создаваемый из reactive error pipeline.
- `src/main/java/edu/course/webclient/dto/NotificationEvent.java` - DTO для SSE
  stream-а.
- `src/main/resources/application.yml` - default base URL.

## Запуск через Docker Compose

Из корня папки `spring-integration-restclients-openapi` сначала запустите provider:

```bash
docker compose up --build provider-api
```

В соседнем терминале:

```bash
docker compose run --rm webclient-client
```

## Что должно быть видно

В выводе клиента будут:

- запрос списка курсов;
- composed result из `Mono.zip(...)`;
- обработанная ошибка `404`;
- первые три SSE события из `/api/notifications`;
- строки logging filter-а для запросов и ответов.

## Связь с лекцией

- Глава про `WebClient`: показывает отличие значения от pipeline-а, который это
  значение когда-нибудь получит.
- Глава про blocking vs non-blocking: `.block(...)` находится только на границе CLI,
  а не внутри каждого "бизнес-метода".
- Глава про streaming: `bodyToFlux(NotificationEvent.class)` читает SSE поток.
- Глава про ошибки: `onStatus(...)` похож по смыслу на error handler, но живет в
  reactive pipeline.

## Вопросы студентам

- В какой строке HTTP-запрос реально начинает выполняться?
- Почему `Mono<CourseDto>` не является самим `CourseDto`?
- Что произойдет, если убрать `take(3)` при чтении бесконечного stream-а?
- Почему `WebClient` не стоит выбирать только из-за модного API?

## Практические изменения

- Добавить retry для временной ошибки и обсудить риск retry storm.
- Переписать `Mono.zip(...)` на последовательные вызовы и сравнить поведение.
- Добавить новый SSE event в provider-е и прочитать его в этом клиенте.
