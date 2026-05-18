# 07-grpc-client

Учебный gRPC client на Spring Boot и grpc-java.

## Роль в лекции

Этот пример показывает client-side часть gRPC-интеграции. В отличие от REST-клиентов,
здесь студент не пишет URL вручную: generated stub дает typed методы, а сообщения
создаются через protobuf builders.

Главная мысль: gRPC делает контракт более строгим на уровне компиляции, но не
отменяет необходимость adapter layer, deadline, mapping ошибок и аккуратного
shutdown channel-а.

## Что изучается

- Использование generated blocking stub.
- Unary RPC: получение списка и одного курса.
- Server streaming RPC: чтение нескольких событий.
- Обработку gRPC-ошибки `NOT_FOUND`.
- Канал как Spring bean с корректным shutdown.
- `ManagedChannel` как сетевое соединение к provider-у.
- `withDeadlineAfter(...)` как обязательный production-паттерн.

## Как подключен gRPC client

1. Клиент содержит тот же `src/main/proto/course_catalog.proto`, что и provider.
2. `protobuf-maven-plugin` генерирует messages и `CourseCatalogServiceGrpc`.
3. `GrpcClientConfig` создает `ManagedChannel`.
4. `GrpcClientConfig` создает generated `CourseCatalogServiceBlockingStub`.
5. `DemoRunner` вызывает stub и задает `withDeadlineAfter(...)` для каждого RPC.

В production-коде stub лучше спрятать за собственным adapter-классом, чтобы бизнес-слой не зависел от `StatusRuntimeException`, `ManagedChannel` и protobuf classes.

## Какие файлы открыть

- `src/main/proto/course_catalog.proto` - копия/общий контракт с provider-ом.
- `pom.xml` - generation plugin для protobuf и gRPC stubs.
- `src/main/java/edu/course/grpc/client/config/GrpcClientConfig.java` -
  `ManagedChannel` и generated blocking stub как Spring beans.
- `src/main/java/edu/course/grpc/client/config/CourseGrpcProperties.java` - host и
  port через configuration properties.
- `src/main/java/edu/course/grpc/client/DemoRunner.java` - unary вызовы, deadline,
  обработка `StatusRuntimeException`, чтение stream-а.
- `src/main/resources/application.yml` - default host/port.

## Запуск через Docker Compose

Из корня папки `spring-integration-restclients-openapi` сначала запустите provider:

```bash
docker compose up --build grpc-provider
```

В соседнем терминале:

```bash
docker compose run --rm grpc-client
```

## Что должно быть видно

В выводе клиента будут:

- список курсов из `ListCourses`;
- один курс из `GetCourse`;
- обработанный `NOT_FOUND` для `missing-course`;
- первые события из server streaming `WatchCourseEvents`.

## Связь с лекцией

- Глава про generated stubs: `CourseCatalogServiceBlockingStub` создан из `.proto`.
- Глава про deadlines: каждый RPC в `DemoRunner` ограничен временем.
- Глава про gRPC errors: `StatusRuntimeException` содержит gRPC status code и
  description.
- Глава про integration adapter: в production этот runner стоит превратить в
  `CourseCatalogGateway`, который отдаст бизнес-коду доменные типы.

## Вопросы студентам

- Чем generated stub отличается от `RestClient`, где URI пишется строкой?
- Почему deadline ставится на каждый RPC, а не только на channel?
- Что случится, если provider обновит `.proto`, а client останется на старой версии?
- Где лучше переводить `Status.NOT_FOUND` в доменную ошибку приложения?

## Практические изменения

- Добавить adapter class поверх generated stub.
- Добавить `INVALID_ARGUMENT` на provider-е и обработать его в client-е.
- Заменить blocking stub на async/future stub и обсудить изменение модели кода.
