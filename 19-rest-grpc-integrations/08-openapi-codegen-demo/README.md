# 08-openapi-codegen-demo

Пример показывает, что OpenAPI-схема может быть не только документацией, но и входом для генерации кода.

## Роль в лекции

Этот пример закрывает практический вопрос из блока про OpenAPI: как из схемы
получить Java client или Spring server contract. Он не заменяет ручные примеры на
`RestTemplate`, `RestClient` и `WebClient`, а показывает следующий уровень зрелости:
контракт становится источником code artifacts.

Главная мысль: generated code полезен, но вокруг него все равно нужен
integration adapter, где живут timeout, auth, retry, logging, mapping ошибок и
доменные типы.

## Что генерируется

- REST-клиент Java из `course-api.yaml`: `target/generated-sources/openapi-client`.
- Spring MVC server contract из той же схемы: `target/generated-sources/openapi-server`.

В учебном проекте generated sources не подключаются к компиляции приложения. Это сделано специально: demo показывает, какие файлы появляются и как их читать, а не заставляет студентов сразу принимать generated code как основной стиль проекта.

## Что изучается

- `openapi-generator-maven-plugin`.
- Разные generator targets из одной схемы: `java` client и `spring` server.
- `operationId` как имя generated method-а.
- `apiPackage`, `modelPackage`, `invokerPackage`.
- `interfaceOnly=true` для server-side contract.
- Почему generated code не обязан попадать прямо в main source set.

## Какие файлы открыть

- `src/main/resources/openapi/course-api.yaml` - schema-first контракт.
- `pom.xml` - две execution секции:
  `generate-rest-client` и `generate-spring-server-contract`.
- `src/main/java/edu/course/codegen/OpenApiCodegenApplication.java` - отчет о
  generated files и marker-строках.
- `target/generated-sources/openapi-client/.../CoursesApi.java` - generated REST
  client API после запуска.
- `target/generated-sources/openapi-server/.../CoursesApi.java` - generated Spring
  controller interface после запуска.

## Запуск через Docker Compose

Из корня папки `spring-integration-restclients-openapi`:

```bash
docker compose run --rm openapi-codegen-demo
```

Provider для этого примера не нужен: генерация идет из локального YAML-файла.

## Что должно быть видно

В выводе приложение печатает:

- root directory generated sources;
- generated REST client API file;
- generated REST client model file;
- generated Spring controller interface file;
- marker lines вроде `listCourses`, `getCourse`, `@RequestMapping`.

## Как использовать в лекции

После блока про OpenAPI сначала показать сырой контракт, затем этот проект:

```text
OpenAPI YAML -> generated Java client
OpenAPI YAML -> generated Spring controller interface
```

Главный вывод: generated code не отменяет adapter. В боевом коде поверх generated client обычно оставляют тонкий `CourseProviderClient`, который задает timeout, retry, auth, mapping ошибок и доменную модель.

## Связь с лекцией

- Глава про OpenAPI generation: это основной runnable-пример для генерации клиента и
  server contract.
- Глава про adapter layer: generated `CoursesApi` не должен растекаться по
  бизнес-коду.
- Глава про evolution контрактов: изменение YAML меняет generated code и может
  сломать потребителей.
- Глава про сравнение REST/gRPC: OpenAPI генерирует REST artifacts, а gRPC
  генерирует protobuf messages и stubs из `.proto`.

## Вопросы студентам

- Какие имена методов появятся, если убрать или переименовать `operationId`?
- Почему server contract interface не равен готовой бизнес-реализации?
- Где вы будете задавать timeout и auth: в generated client-е или adapter-е?
- Какие изменения в `course-api.yaml` потребуют обновления всех клиентов?

## Практические изменения

- Добавить новый endpoint в YAML и проверить, какие generated файлы изменились.
- Поменять schema `Course` и посмотреть, как изменилась model class.
- Изменить `library` у Java client-а и обсудить, что меняется в generated code.
