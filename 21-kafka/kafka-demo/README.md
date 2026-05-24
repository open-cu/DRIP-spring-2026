# kafka-demo

Демо-проект к лекции 21 (`Kafka + Spring Kafka`) в формате **Gradle multi-module**.

## Быстрый старт
Из папки `kafka-demo`:

```bash
docker compose up -d
```

После запуска Kafka:
- broker: `localhost:9092`
- Kafka UI: `http://localhost:8088`

Остановить:

```bash
docker compose down
```

## Модули
- `common-domain` - общие модели и контракты
- `test-support` - общий testcontainers/test infra
- `kafka-core-app` - Spring Kafka core demo + Kafka Streams extension
- `kafka-distribution-model-app` - topic/partition/key/consumer group demo
- `kafka-delayed-processing-app` - delayed processing demo + retry topics + DLT
- `kafka-batch-retry-app` - batch + partial failure + DLT demo

## Запуск модулей
Запускайте один app-модуль за раз. Каждый app-модуль слушает `http://localhost:8080`.

### kafka-core-app
```bash
./gradlew :kafka-core-app:bootRun
```
UI:
- `GET /blocks/kafka-core`
- `GET /blocks/kafka-streams`

Kafka Streams живет внутри `kafka-core-app` и не считается отдельным Gradle-модулем.

### kafka-distribution-model-app
```bash
./gradlew :kafka-distribution-model-app:bootRun
```
UI:
- `GET /blocks/kafka-distribution-model`

### kafka-delayed-processing-app
```bash
./gradlew :kafka-delayed-processing-app:bootRun
```
UI:
- `GET /blocks/kafka-delayed-processing`

### kafka-batch-retry-app
```bash
./gradlew :kafka-batch-retry-app:bootRun
```
UI:
- `GET /blocks/kafka-batch-retry`

## Тесты
Запуск UI-flow тестов всех app-модулей:

```bash
./gradlew :kafka-core-app:test :kafka-distribution-model-app:test :kafka-delayed-processing-app:test :kafka-batch-retry-app:test
```

Интеграционные тесты используют Testcontainers и требуют доступный Docker.
