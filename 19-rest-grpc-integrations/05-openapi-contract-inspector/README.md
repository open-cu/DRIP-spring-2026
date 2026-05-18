# 05-openapi-contract-inspector

CLI-приложение, которое читает OpenAPI-контракт и печатает краткий отчет.

## Роль в лекции

Этот пример переводит разговор про OpenAPI из режима "красивая Swagger UI-страница"
в режим "контракт можно читать инструментами". Он показывает, что OpenAPI-документ
может быть входом для проверок, ревью совместимости, генерации клиентов и
документации.

## Что изучается

- OpenAPI - это не только Swagger UI, а машинно-читаемый контракт.
- Контракт можно читать из локального YAML-файла.
- Контракт можно читать из живого `/v3/api-docs` работающего Spring Boot-приложения.
- `swagger-parser` разбирает paths, operations, responses и schemas.
- `operationId` важен для генерации клиентов и читаемой документации.

## Какие файлы открыть

- `src/main/java/edu/course/openapi/ContractReportRunner.java` - парсинг OpenAPI и
  печать отчета.
- `src/main/java/edu/course/openapi/config/OpenApiProperties.java` - настройка
  источника контракта через `OPENAPI_SOURCE`.
- `src/main/resources/openapi/course-api.yaml` - локальная schema-first версия
  контракта.
- `pom.xml` - dependency на `swagger-parser`.

## Запуск с живым контрактом API-поставщика

Из корня папки `spring-integration-restclients-openapi` сначала запустите provider:

```bash
docker compose up --build provider-api
```

В соседнем терминале:

```bash
docker compose run --rm openapi-contract-inspector
```

По умолчанию контейнер читает `http://provider-api:8081/v3/api-docs`.

## Запуск с локальным контрактом внутри образа

Из корня папки `spring-integration-restclients-openapi`:

```bash
docker compose run --rm -e OPENAPI_SOURCE=classpath:/openapi/course-api.yaml openapi-contract-inspector
```

## Что должно быть видно

Приложение печатает:

- источник контракта;
- title и version;
- список paths и HTTP methods;
- `operationId` и summary;
- response codes;
- список schemas.

Если в контракте есть parse warnings, они выводятся с префиксом `[parser]`.

## Связь с лекцией

- Глава про OpenAPI: демонстрирует, что `/v3/api-docs` - главный артефакт, а
  Swagger UI только визуализирует его.
- Глава про contract-first/code-first: один и тот же inspector умеет читать live
  contract и локальный YAML.
- Глава про генерацию: `08-openapi-codegen-demo` использует OpenAPI уже не для
  отчета, а для создания Java code artifacts.
- Глава про API evolution: список paths/responses/schemas можно сравнивать между
  версиями в CI.

## Вопросы студентам

- Какие изменения в YAML будут breaking changes для клиента?
- Почему `operationId` важен для generated client-а?
- Чем live `/v3/api-docs` отличается от YAML, который лежит в репозитории?
- Какие проверки вы бы добавили в CI поверх такого parser-а?

## Практические изменения

- Добавить новый response code в provider и проверить, появился ли он в отчете.
- Изменить локальный YAML и сравнить отчет с live `/v3/api-docs`.
- Добавить проверку, что у каждой operation есть `operationId`.
