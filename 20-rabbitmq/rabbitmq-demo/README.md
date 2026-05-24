# rabbitmq-demo

Демо-проект к лекции 20 (`RabbitMQ + Spring AMQP`) в формате **Gradle multi-module**.

## Быстрый старт
Из папки `rabbitmq-demo`:

```bash
docker compose up -d
```

После запуска RabbitMQ:
- AMQP: `localhost:5672`
- Management UI: `http://localhost:15672`
- login/password: `guest/guest`

Остановить:

```bash
docker compose down
```

## Модули
- `common-domain` — общие модели и контракты
- `test-support` — общий testcontainers/test infra
- `routing-basics-app` — routing demo
- `spring-core-app` — Spring AMQP core demo
- `delayed-delivery-app` — delayed delivery demo
- `batch-retry-app` — batch + retry demo

## Запуск модулей

### routing-basics-app
```bash
./gradlew :routing-basics-app:bootRun
```
UI:
- `GET /blocks/routing-basics`

### spring-core-app
```bash
./gradlew :spring-core-app:bootRun
```
UI:
- `GET /blocks/spring-core`
- `GET /blocks/spring-core/new`

### delayed-delivery-app
```bash
./gradlew :delayed-delivery-app:bootRun
```
UI:
- `GET /blocks/delayed-delivery`

### batch-retry-app
```bash
./gradlew :batch-retry-app:bootRun
```
UI:
- `GET /blocks/batch-retry`

## Тесты
Запуск UI-flow тестов всех app-модулей:

```bash
./gradlew :routing-basics-app:test :spring-core-app:test :delayed-delivery-app:test :batch-retry-app:test
```
