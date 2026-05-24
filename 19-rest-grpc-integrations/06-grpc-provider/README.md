# 06-grpc-provider

Учебный gRPC provider на Spring Boot и grpc-java.

## Роль в лекции

Этот пример добавляет к теме интеграций не-REST подход. Он показывает, как сервис
может публиковать typed RPC contract через Protocol Buffers и grpc-java, а Spring
Boot использоваться как контейнер для lifecycle, конфигурации и dependency injection.

Главная мысль: в gRPC внешней границей является `.proto`, generated classes и
gRPC status codes, а не Spring MVC controller, JSON DTO и HTTP status codes.

## Что изучается

- `.proto` как контракт между сервисами.
- Генерацию Java-кода из Protocol Buffers.
- gRPC server lifecycle как Spring bean.
- Unary RPC: `GetCourse`, `ListCourses`.
- Server streaming RPC: `WatchCourseEvents`.
- Ошибку `NOT_FOUND` через `StatusRuntimeException`.
- `StreamObserver` как server-side способ отправить response или error.
- Отличие gRPC service implementation от Spring MVC controller.

## Как подключен gRPC

1. `src/main/proto/course_catalog.proto` описывает service, rpc methods и messages.
2. `protobuf-maven-plugin` запускает `protoc` и `protoc-gen-grpc-java`.
3. Maven кладет generated classes в:

```text
target/generated-sources/protobuf/java
target/generated-sources/protobuf/grpc-java
```

4. `CourseCatalogGrpcService` наследуется от generated `CourseCatalogServiceImplBase`.
5. `GrpcServerLifecycle` поднимает `io.grpc.Server` как Spring lifecycle bean.

Это не Spring MVC controller. В gRPC endpoint-ом становится generated service implementation, добавленная в `ServerBuilder`.

## Какие файлы открыть

- `src/main/proto/course_catalog.proto` - service, rpc methods, messages, enum и
  field numbers.
- `pom.xml` - `protobuf-maven-plugin`, `protoc` и `protoc-gen-grpc-java`.
- `src/main/java/edu/course/grpc/provider/CourseCatalogGrpcService.java` -
  реализация generated base class.
- `src/main/java/edu/course/grpc/provider/GrpcServerLifecycle.java` - старт и
  graceful shutdown grpc-java server-а через Spring lifecycle.
- `src/main/java/edu/course/grpc/provider/CourseRepository.java` - учебные данные,
  которые превращаются в protobuf messages.
- `src/main/resources/application.yml` - порт gRPC server-а.

## Запуск через Docker Compose

Из корня папки `spring-integration-restclients-openapi`:

```bash
docker compose up --build grpc-provider
```

gRPC-сервер слушает порт `9090`.

Для полного сценария во втором терминале запустите client:

```bash
docker compose run --rm grpc-client
```

## Что должно быть видно

В логах provider-а должна появиться строка:

```text
gRPC provider started on port 9090
```

После запуска клиента provider обработает:

- unary request списка курсов;
- unary request одного курса;
- unary request missing course с gRPC status `NOT_FOUND`;
- server streaming request с несколькими событиями.

## Связь с лекцией

- Глава про gRPC contract: `.proto` заменяет OpenAPI для RPC-взаимодействия.
- Глава про generated code: Maven генерирует messages и service base class.
- Глава про server lifecycle: Spring управляет жизнью grpc-java server-а.
- Глава про streaming: `WatchCourseEvents` показывает server streaming без SSE.

## Вопросы студентам

- Почему нельзя бездумно менять field numbers в `.proto`?
- Где в gRPC примере аналог `404` из REST provider-а?
- Чем `StreamObserver.onError(...)` отличается от выбрасывания обычного exception?
- Почему service implementation наследуется от generated class?

## Практические изменения

- Добавить поле `level` в `Course` с новым field number и посмотреть generated code.
- Добавить новый unary RPC `GetCourseStats`.
- Изменить `WatchCourseEvents` так, чтобы `limit <= 0` возвращал `INVALID_ARGUMENT`.
