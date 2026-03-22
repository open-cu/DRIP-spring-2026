# Spring Shell: Создание интерактивных CLI-приложений

## 1. Проблемы, которые решает Spring Shell, REPL

### 1.1. Проблематика создания CLI-приложений

При разработке консольных приложений (CLI) разработчики традиционно сталкиваются с рядом повторяющихся проблем:

| Проблема                 | Описание                                                                        | Ручное решение                                                                                        |
| ------------------------ | ------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| **Парсинг аргументов**   | Разбор строки команд, обработка флагов (`-f`, `--file`), позиционных аргументов | Написание собственного парсера или использование сторонних библиотек (Apache Commons CLI, JCommander) |
| **Валидация ввода**      | Проверка типов, обязательности параметров, диапазонов значений                  | Ручная проверка каждого параметра с выбросом исключений                                               |
| **Формирование справки** | Генерация `--help` с описанием всех команд и их параметров                      | Поддержка документации в актуальном состоянии вручную                                                 |
| **Автодополнение**       | Tab-completion для команд и аргументов                                          | Реализация сложной логики автодополнения                                                              |
| **Вывод информации**     | Форматирование таблиц, цветовое выделение, прогресс-бары                        | Написание собственных утилит форматирования                                                           |
| **Обработка ошибок**     | Единообразное представление исключений пользователю                             | try-catch блоки и преобразование исключений                                                           |
| **Состояние сессии**     | Сохранение контекста между командами (аутентификация, настройки)                | Реализация механизма хранения состояния                                                               |

Без специализированного фреймворка разработчик вынужден:
- Писать большое количество **шаблонного кода**
- **Дублировать** решения для разных команд
- Поддерживать **документацию** и код в актуальном состоянии
- Реализовывать **интерактивность** (история команд, редактирование строки) с нуля

### 1.2. Что такое REPL?

**REPL** (Read-Eval-Print Loop) - это архитектурный паттерн интерактивных приложений, состоящий из четырех этапов:

| READ    | EVAL       | PRINT      | LOOP   |
| ------- | ---------- | ---------- | ------ |
| чтение  | выполнение | вывод      | повтор |
| команды | команды    | результата | цикла  |

### 1.3. Что предлагает Spring Shell?

**Spring Shell** - это фреймворк из экосистемы Spring, который:

1. **Автоматизирует создание REPL-приложений**
   - Предоставляет готовый цикл Read-Eval-Print
   - Управляет историей команд

2. **Декларативно определяет команды**
   - Через аннотации (`@ShellMethod`, `@ShellComponent`)
   - Или программно (`CommandRegistration`)

3. **Интегрируется с Spring Boot**
   - Использует Dependency Injection
   - Поддерживает конфигурацию через `application.yml`
   - Работает с профилями Spring

4. **Предоставляет встроенные возможности**
   - Автоматическая справка (`help`)
   - История команд (`history`)
   - Обработка ошибок (`stacktrace`)
   - Выход из приложения (`quit`, `exit`)

### 1.4. Ключевые преимущества Spring Shell

| Преимущество                 | Описание                                                         |
| ---------------------------- | ---------------------------------------------------------------- |
| **Минимальный код**          | Создание команды занимает 3-5 строк кода                         |
| **Типизированные параметры** | Параметры команд преобразуются в Java-типы автоматически         |
| **Встроенный help**          | Документация формируется из аннотаций                            |
| **Гибкость**                 | Поддержка интерактивного, неинтерактивного и скриптового режимов |

---

## 2. Подключение стартера в Maven, главный класс приложения

### 2.1. Создание проекта

Для работы с Spring Shell в Spring Boot 3.4.x необходимо создать проект с соответствующими зависимостями.

#### 2.1.1. Параметры проекта

| Параметр | Значение |
|----------|----------|
| **Group ID** | ru.cu |
| **Artifact ID** | spring-shell-demo |
| **Spring Boot** | 3.4.x |
| **Java** | 17 или выше |
| **Сборщик** | Maven |

### 2.2. Подключение зависимостей

В файл `pom.xml` необходимо добавить зависимости Spring Shell.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.5.12</version>
		<relativePath/>
	</parent>
	<groupId>ru.cu</groupId>
	<artifactId>spring-shell-demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>spring-shell-demo</name>
	<description>Demo project for Spring Shell</description>

	<properties>
		<java.version>17</java.version>
		<spring-shell.version>3.4.1</spring-shell.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.shell</groupId>
			<artifactId>spring-shell-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.shell</groupId>
				<artifactId>spring-shell-dependencies</artifactId>
				<version>${spring-shell.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

### 2.3. Главный класс приложения

Главный класс не отличается от стандартного Spring Boot приложения за исключением случая, когда используется современный подход к описанию команд.

```java
package ru.cu.springshelldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.EnableCommand;

import ru.cu.springshelldemo.command.Example07ModernUserCommand;
import ru.cu.springshelldemo.command.Example08ModernTypedParametersCommand;
import ru.cu.springshelldemo.command.Example09ModernTextOutputCommand;
import ru.cu.springshelldemo.command.Example10ModernTableOutputCommand;
import ru.cu.springshelldemo.command.Example11ModernAvailabilityCommand;
import ru.cu.springshelldemo.command.Example17ModeAwareCommand;

@SpringBootApplication
public class SpringShellDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringShellDemoApplication.class, args);
	}
}
```

### 2.4. Базовая конфигурация

Для настройки Spring Shell можно использовать `application.yml` или `application.properties`.

```yaml
spring:
  shell:
    interactive:
      enabled: true
    noninteractive:
      enabled: true
    script:
      enabled: true
    history:
      enabled: true
      name: .shell_history
```

### 2.5. Важные замечания о тестах

При добавлении `spring-shell-starter` тест, сгенерированный Spring Initializr, будет **зависать**, так как Spring Shell запускает интерактивную оболочку. Решение - отключить интерактивный режим в тестах.

```java
package ru.cu.springshelldemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.shell.interactive.enabled=false")
class SpringShellDemoApplicationTests {

    @Test
    void contextLoads() {
    }
}
```
### 2.6. Проверка работоспособности

Для проверки создайте простейшую команду и запустите приложение:

```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class Example01HelloCommand {

	@ShellMethod(key = "hello", value = "Say hello")
	public String hello() {
		return "Hello from Spring Shell!";
	}
}
```

Ожидаемый результат.

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.5.12)

shell:>hello
Hello from Spring Shell!
shell:>
```

---

## 3. Работа с фреймворком, legacy подход (@ShellMethod)

### 3.1. Общая концепция legacy подхода

Legacy подход в Spring Shell (до версии 3.x) базируется на аннотациях из пакета `org.springframework.shell.standard`. Хотя этот подход считается устаревающим (deprecated) и будет удален в будущих версиях, он все еще широко используется в существующих проектах и является хорошей отправной точкой для понимания основ фреймворка.

**Ключевые аннотации:**
- `@ShellComponent` - маркирует класс как контейнер для shell-команд
- `@ShellMethod` - определяет метод как команду
- `@ShellOption` - описывает параметры команды

### 3.2. Описание команд и их параметров

Команда в legacy подходе - это обычный метод Java, аннотированный `@ShellMethod`. Параметры метода автоматически преобразуются из строкового ввода в соответствующие Java-типы.

**Атрибуты `@ShellMethod`:**
- `key` - имя команды (если не указано, используется имя метода)
- `value` - описание команды, отображаемое в справке
- `prefix` - префикс для опций (по умолчанию `-` или `--`)
- `group` - группа для группировки команд в справке (доступно с Spring Shell 2.x)

**Атрибуты `@ShellOption`:**
- `value` - имена опций (например, `--name`)
- `defaultValue` - значение по умолчанию
- `arity` - количество аргументов, принимаемых опцией
- `help` - описание параметра для справки

Пример
```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class Example02UserCommand {

	@ShellMethod(key = "user-info", value = "Display user information")
	public String userInfo(
			@ShellOption(value = { "-n", "--name" }, defaultValue = "Guest", help = "User name") String name,

			@ShellOption(value = { "-a", "--age" }, defaultValue = "18", help = "User age") int age,

			@ShellOption(value = { "-e", "--email" }, help = "User email") String email) {
		return String.format("User: %s, Age: %d, Email: %s", name, age, email);
	}
}
```

Использование
```
shell:>user-info --name Ivan --age 25 --email ivan@example.com
User: Ivan, Age: 25, Email: ivan@example.com

shell:>user-info -e petrov@example.com
User: Guest, Age: 18, Email: petrov@example.com
```

### 3.3. Типизация параметров

Одной из ключевых особенностей Spring Shell является **автоматическая типизация параметров**. Фреймворк использует встроенные конвертеры для преобразования строковых аргументов в нужные типы.

**Поддерживаемые типы из коробки:**
- Примитивы и их обертки (`int`, `Integer`, `boolean`, `Boolean` и т.д.)
- `String`
- `BigDecimal`, `BigInteger`
- `java.io.File`
- `java.time.LocalDate`, `LocalDateTime`, `LocalTime`
- Массивы и коллекции

Пример
```java
package ru.cu.springshelldemo.command;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class Example03TypedParametersCommand {

	@ShellMethod(key = "calc", value = "Perform calculations")
	public String calculate(
			@ShellOption(help = "First number") int a,
			@ShellOption(help = "Second number") double b,
			@ShellOption(help = "Multiplier") BigDecimal multiplier) {
		BigDecimal result = BigDecimal.valueOf(a + b).multiply(multiplier);
		return String.format("Result: %s", result);
	}

	@ShellMethod(key = "date-info", value = "Process date")
	public String dateInfo(
			@ShellOption(help = "Some date") LocalDate date,
			@ShellOption(help = "Enable flag") boolean enabled) {
		return String.format("Date: %s, Enabled: %s", date, enabled);
	}

	@ShellMethod(key = "file-info", value = "Get file info")
	public String fileInfo(
			@ShellOption(help = "Path to file") File file) {
		return String.format("File: %s, Exists: %s, Size: %d bytes",
				file.getAbsolutePath(),
				file.exists(),
				file.exists() ? file.length() : 0);
	}
}
```

Использование
```
shell:>calc --a 10 --b 20.5 --multiplier 2.5
Result: 76.25

shell:>date-info --date 2024-03-22 --enabled true
Date: 2024-03-22, Enabled: true

shell:>file-info --file C:\Windows\System32\drivers\etc\hosts
File: C:\Windows\System32\drivers\etc\hosts, Exists: true, Size: 1234 bytes
```

### 3.4. Вывод данных командой

Spring Shell поддерживает различные способы вывода информации.

#### 3.4.1. Простой текстовый вывод

Метод команды может возвращать `String`, который будет выведен в консоль.

Пример
```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class Example04TextOutputCommand {

	@ShellMethod(key = "greet", value = "Greeting message")
	public String greet(
			@ShellOption(defaultValue = "World") String name) {
		return "Hello, " + name + "! Welcome to Spring Shell!";
	}

	@ShellMethod(key = "multiline", value = "Multiline output")
	public String multiline() {
		return "Line 1\nLine 2\nLine 3";
	}
}
```

Использование
```
shell:>greet --name Alice
Hello, Alice! Welcome to Spring Shell!

shell:>multiline
Line 1
Line 2
Line 3
```

#### 3.4.2. Вывод в виде таблиц

Для вывода структурированных данных Spring Shell предоставляет API для построения ASCII-таблиц.

**Ключевые классы для работы с таблицами:**
- `TableModelBuilder` - построитель модели таблицы
- `Table` - сама таблица
- `TableModel` - модель данных таблицы

Пример
```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;

@ShellComponent
public class Example05TableOutputCommand {

	@ShellMethod(key = "show-users", value = "Show users table")
	public Table showUsers() {
		TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

		modelBuilder.addRow()
				.addValue("ID")
				.addValue("Name")
				.addValue("Email")
				.addValue("Age");

		modelBuilder.addRow()
				.addValue("1")
				.addValue("Ivan Petrov")
				.addValue("ivan@example.com")
				.addValue("25");

		modelBuilder.addRow()
				.addValue("2")
				.addValue("Maria Sidorova")
				.addValue("maria@example.com")
				.addValue("30");

		modelBuilder.addRow()
				.addValue("3")
				.addValue("Alexey Smirnov")
				.addValue("alexey@example.com")
				.addValue("28");

		TableModel model = modelBuilder.build();

		return new TableBuilder(model)
				.on(CellMatchers.column(0)).addAligner(SimpleHorizontalAligner.center).and()
				.on(CellMatchers.column(1)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(2)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(3)).addAligner(SimpleHorizontalAligner.center).and()
				.addFullBorder(BorderStyle.fancy_light)
				.build();
	}

	@ShellMethod(key = "show-products", value = "Show products table")
	public Table showProducts() {
		TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

		modelBuilder.addRow().addValue("Product").addValue("Price").addValue("Stock");
		modelBuilder.addRow().addValue("Laptop").addValue("75000 ₽").addValue("15");
		modelBuilder.addRow().addValue("Mouse").addValue("2500 ₽").addValue("100");
		modelBuilder.addRow().addValue("Keyboard").addValue("4500 ₽").addValue("45");

		TableModel model = modelBuilder.build();

		return new TableBuilder(model)
				.on(CellMatchers.column(0)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(1)).addAligner(SimpleHorizontalAligner.right).and()
				.on(CellMatchers.column(2)).addAligner(SimpleHorizontalAligner.center).and()
				.addFullBorder(BorderStyle.fancy_light)
				.build();
	}
}
```

Использование
```
shell:>show-users
+----+----------------+-------------------+-----+
| ID |      Name      |       Email       | Age |
+----+----------------+-------------------+-----+
| 1  | Ivan Petrov    | ivan@example.com  | 25  |
| 2  | Maria Sidorova | maria@example.com | 30  |
| 3  | Alexey Smirnov | alexey@example.com| 28  |
+----+----------------+-------------------+-----+

shell:>show-products
+----------+---------+-------+
| Product  |  Price  | Stock |
+----------+---------+-------+
| Laptop   | 75000 ₽ |  15   |
| Mouse    | 2500 ₽  |  100  |
| Keyboard | 4500 ₽  |  45   |
+----------+---------+-------+
```

### 3.5. Управление доступностью команд

Spring Shell позволяет динамически управлять доступностью команд в зависимости от состояния приложения (например, аутентификация пользователя, наличие подключения к базе данных и т.д.).

**Механизм доступности:**
1. Создается метод-провайдер, возвращающий `Availability`
2. Метод связывается с командой через `@ShellMethodAvailability`

**Варианты доступности:**
- `Availability.available()` - команда доступна
- `Availability.unavailable("причина")` - команда недоступна с указанием причины

Пример
```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class Example06AvailabilityCommand {

	private boolean authenticated = false;
	private String currentUser = null;

	@ShellMethod(key = "login", value = "Login to system")
	public String login(
			@ShellOption(help = "Username") String username,
			@ShellOption(help = "Password") String password) {
		if ("admin".equals(username) && "secret".equals(password)) {
			authenticated = true;
			currentUser = username;
			return "Login successful! Welcome, " + username;
		}
		return "Login failed! Invalid credentials.";
	}

	@ShellMethod(key = "logout", value = "Logout from system")
	public String logout() {
		if (authenticated) {
			authenticated = false;
			currentUser = null;
			return "Logout successful!";
		}
		return "You are not logged in.";
	}

	@ShellMethod(key = "secret", value = "Secret command")
	public String secretCommand() {
		return "This is secret information!";
	}

	@ShellMethodAvailability("secret")
	public Availability secretAvailability() {
		return authenticated
				? Availability.available()
				: Availability.unavailable("You need to login first. Use 'login' command.");
	}

	@ShellMethod(key = "status", value = "Show status")
	public String status() {
		return authenticated
				? "Authenticated as: " + currentUser
				: "Not authenticated";
	}
}
```
---
## 4. Работа с фреймворком, современный подход (@Command)

### 4.1. Общая концепция современного подхода

Начиная с Spring Shell 3.x, появились два способа создания команд, которые считаются современными и рекомендуемыми:

1. **Аннотационный подход через `@Command`** - декларативное определение команд с помощью аннотаций (аналогично `@ShellMethod`, но с расширенными возможностями)
2. **Программный подход через `CommandRegistration`** - явная регистрация команд с помощью билдера

Оба подхода являются **актуальными** и поддерживаются в Spring Shell 3.x, в отличие от legacy подхода (`@ShellMethod`), который объявлен устаревшим.

### 4.2. Аннотационный подход (@Command)

#### 4.2.1. Описание команд и их параметров

Аннотация `@Command` является современной заменой `@ShellMethod`. Она предоставляет расширенные возможности и лучше интегрируется с функциональностью Spring Shell.

**Ключевые аннотации:**
- `@Command` - аналог `@ShellMethod`, определяет команду
- `@Option` - аналог `@ShellOption`, описывает параметры
- `@CommandScan/@EnableCommand` - используются для регистрации команд, объявленных через `@Command`

**Атрибуты `@Command`:**
- `command` - имя команды (обязательный)
- `description` - описание команды
- `alias` - псевдонимы команды
- `group` - группа для группировки в справке

**Атрибуты `@Option`:**
- `longNames/shortNames` - имена опций (например, `{"--name", "-n"}`)
- `description` - описание параметра
- `required` - обязательность параметра
- `defaultValue` - значение по умолчанию
- `arity` - количество аргументов

```java
// Над конфигом (например, над главным классом Spring Boot приложения)
@EnableCommand({
		Example11ModernAvailabilityCommand.class,
		Example10ModernTableOutputCommand.class,
		Example09ModernTextOutputCommand.class,
		Example08ModernTypedParametersCommand.class,
		Example07ModernUserCommand.class,
		Example17ModeAwareCommand.class
})
```

```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command
public class Example07ModernUserCommand {

	@Command(command = "m-user-info", description = "Display user information")
	public String userInfo(
			@Option(longNames = "name", shortNames = 'n', description = "User name", defaultValue = "Guest") String name,
			@Option(longNames = "age", shortNames = 'a', description = "User age", defaultValue = "18") int age,
			@Option(longNames = "email", shortNames = 'e', description = "User email", required = true) String email) {
		return String.format("User: %s, Age: %d, Email: %s", name, age, email);
	}
}
```

#### 4.2.2. Типизация параметров

Как и в других подходах, параметры в `@Command` **типизированы** и автоматически преобразуются в нужные Java-типы.

```java
package ru.cu.springshelldemo.command;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command
public class Example08ModernTypedParametersCommand {

	@Command(command = "m-calc", description = "Perform calculations")
	public String calculate(
			@Option(longNames = "a", description = "First number", required = true) int a,
			@Option(longNames = "b", description = "Second number", required = true) double b,
			@Option(longNames = "multiplier", description = "Multiplier", required = true) BigDecimal multiplier) {
		BigDecimal result = BigDecimal.valueOf(a + b).multiply(multiplier);
		return String.format("Result: %s", result);
	}

	@Command(command = "m-date-info", description = "Process date")
	public String dateInfo(
			@Option(longNames = "date", description = "Some date", required = true) LocalDate date,
			@Option(longNames = "enabled", description = "Enable flag", defaultValue = "false") boolean enabled) {
		return String.format("Date: %s, Enabled: %s", date, enabled);
	}

	@Command(command = "m-file-info", description = "Get file info")
	public String fileInfo(
			@Option(longNames = "file", description = "Path to file", required = true) File file) {
		return String.format("File: %s, Exists: %s, Size: %d bytes",
				file.getAbsolutePath(),
				file.exists(),
				file.exists() ? file.length() : 0);
	}

	@Command(command = "m-process-ids", description = "Process multiple IDs")
	public String processIds(
			@Option(longNames = "ids", description = "List of IDs", required = true) List<Integer> ids) {
		return "Processed IDs: " + ids.stream().map(String::valueOf).collect(Collectors.joining(", "));
	}
}
```

#### 4.2.3. Вывод данных командой

Поддерживаются те же механизмы вывода: строки, таблицы, форматированный вывод.

```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command
public class Example09ModernTextOutputCommand {

	@Command(command = "m-greet", description = "Greeting message")
	public String greet(
			@Option(longNames = "name", shortNames = 'n', description = "Name to greet", defaultValue = "World") String name) {
		return "Hello, " + name + "! Welcome to Spring Shell!";
	}

	@Command(command = "m-multiline", description = "Multiline output")
	public String multiline() {
		return "Line 1\nLine 2\nLine 3";
	}
}
```

```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;

@Command
public class Example10ModernTableOutputCommand {

	@Command(command = "m-show-users", description = "Show users table")
	public Table showUsers() {
		TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

		modelBuilder.addRow()
				.addValue("ID")
				.addValue("Name")
				.addValue("Email")
				.addValue("Age");

		modelBuilder.addRow()
				.addValue("1")
				.addValue("Ivan Petrov")
				.addValue("ivan@example.com")
				.addValue("25");

		modelBuilder.addRow()
				.addValue("2")
				.addValue("Maria Sidorova")
				.addValue("maria@example.com")
				.addValue("30");

		modelBuilder.addRow()
				.addValue("3")
				.addValue("Alexey Smirnov")
				.addValue("alexey@example.com")
				.addValue("28");

		TableModel model = modelBuilder.build();
		return new TableBuilder(model)
				.on(CellMatchers.column(0)).addAligner(SimpleHorizontalAligner.center).and()
				.on(CellMatchers.column(1)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(2)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(3)).addAligner(SimpleHorizontalAligner.center).and()
				.addFullBorder(BorderStyle.fancy_light)
				.build();
	}

	@Command(command = "m-show-products", description = "Show products table")
	public Table showProducts() {
		TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

		modelBuilder.addRow().addValue("Product").addValue("Price").addValue("Stock");
		modelBuilder.addRow().addValue("Laptop").addValue("75000 ₽").addValue("15");
		modelBuilder.addRow().addValue("Mouse").addValue("2500 ₽").addValue("100");
		modelBuilder.addRow().addValue("Keyboard").addValue("4500 ₽").addValue("45");

		TableModel model = modelBuilder.build();
		return new TableBuilder(model)
				.on(CellMatchers.column(0)).addAligner(SimpleHorizontalAligner.left).and()
				.on(CellMatchers.column(1)).addAligner(SimpleHorizontalAligner.right).and()
				.on(CellMatchers.column(2)).addAligner(SimpleHorizontalAligner.center).and()
				.addFullBorder(BorderStyle.fancy_light)
				.build();
	}
}
```


#### 4.2.4. Управление доступностью команд

В аннотационном подходе управление доступностью реализуется через бин-провайдер, реализующий интерфейс `AvailabilityProvider`. Имя бина указывается в аннотации `@CommandAvailability`, расположенной над командой, доступностью которой должен управлять провайдер

**Особенности:**
- Метод должен иметь ту же сигнатуру, что и команда
- Возвращает `Availability.available()` или `Availability.unavailable("причина")`
- Аннотация `@CommandAvailability` связывает метод доступности с командой

```java
package ru.cu.springshelldemo.command;

import org.springframework.shell.Availability;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandAvailability;
import org.springframework.shell.command.annotation.Option;

@Command
public class Example11ModernAvailabilityCommand {

	private boolean authenticated = false;
	private String currentUser = null;

	@Command(command = "m-login", description = "Login to system")
	public String login(
			@Option(longNames = "username", description = "Username", required = true) String username,
			@Option(longNames = "password", description = "Password", required = true) String password) {
		if ("admin".equals(username) && "secret".equals(password)) {
			authenticated = true;
			currentUser = username;
			return "Login successful! Welcome, " + username;
		}
		return "Login failed! Invalid credentials.";
	}

	@Command(command = "m-logout", description = "Logout from system")
	public String logout() {
		if (authenticated) {
			authenticated = false;
			currentUser = null;
			return "Logout successful!";
		}
		return "You are not logged in.";
	}

	@Command(command = "m-secret", description = "Secret command")
	@CommandAvailability(provider = "mSecretAvailabilityProvider")
	public String secretCommand() {
		return "This is secret information!";
	}

	@Command(command = "m-status", description = "Show status")
	public String status() {
		return authenticated
				? "Authenticated as: " + currentUser
				: "Not authenticated";
	}

	public Availability evaluateSecretAvailability() {
		return authenticated
				? Availability.available()
				: Availability.unavailable("You need to login first. Use 'm-login' command.");
	}
}
```

```java
package ru.cu.springshelldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.AvailabilityProvider;

import ru.cu.springshelldemo.command.Example11ModernAvailabilityCommand;

@Configuration
public class Example18ModernAvailabilityConfig {

	@Bean
	public AvailabilityProvider mSecretAvailabilityProvider(Example11ModernAvailabilityCommand modernAvailabilityCommand) {
		return modernAvailabilityCommand::evaluateSecretAvailability;
	}
}
```

### 4.3. Программный подход (CommandRegistration)

#### 4.3.1. Описание команд и их параметров

`CommandRegistration` - это билдер для программной регистрации команд.

```java
package ru.cu.springshelldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.CommandRegistration;

@Configuration
public class Example12UserCommandRegistration {

	@Bean
	public CommandRegistration regUserInfoCommand() {
		return CommandRegistration.builder()
				.command("reg-user-info")
				.description("Display user information")
				.group("User Commands (programmatic)")
				.withOption()
					.longNames("name")
					.shortNames('n')
					.description("User name")
					.defaultValue("Guest")
					.and()
				.withOption()
					.longNames("age")
					.shortNames('a')
					.type(Integer.class)
					.description("User age")
					.defaultValue("18")
					.and()
				.withOption()
					.longNames("email")
					.shortNames('e')
					.type(String.class)
					.required(true)
					.description("User email")
					.and()
				.withTarget()
					.function(ctx -> {
						String name = ctx.getOptionValue("name");
						int age = ctx.getOptionValue("age");
						String email = ctx.getOptionValue("email");
						return String.format("User: %s, Age: %d, Email: %s", name, age, email);
					})
				.and()
				.build();
	}
}
```

#### 4.3.2. Типизация параметров

Тип параметра указывается явно через `.type(Class)` (см. **пример 4.8**).

```java
package ru.cu.springshelldemo.config;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.shell.command.CommandRegistration;

@Configuration
public class Example13TypedParametersRegistration {

	@Bean
	public CommandRegistration regCalculateCommand() {
		return CommandRegistration.builder()
				.command("reg-calc")
				.description("Perform calculations")
				.withOption()
					.longNames("a")
					.type(Integer.class)
					.required(true)
					.description("First number")
					.and()
				.withOption()
					.longNames("b")
					.type(Double.class)
					.required(true)
					.description("Second number")
					.and()
				.withOption()
					.longNames("multiplier")
					.type(BigDecimal.class)
					.required(true)
					.description("Multiplier")
					.and()
				.withTarget()
					.function(ctx -> {
						int a = ctx.getOptionValue("a");
						double b = ctx.getOptionValue("b");
						BigDecimal multiplier = ctx.getOptionValue("multiplier");
						BigDecimal result = BigDecimal.valueOf(a + b).multiply(multiplier);
						return String.format("Result: %s", result);
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regDateInfoCommand() {
		return CommandRegistration.builder()
				.command("reg-date-info")
				.description("Process date")
				.withOption()
					.longNames("date")
					.type(LocalDate.class)
					.required(true)
					.description("Some date")
					.and()
				.withOption()
					.longNames("enabled")
					.type(Boolean.class)
					.defaultValue("false")
					.description("Enable flag")
					.and()
				.withTarget()
					.function(ctx -> {
						LocalDate date = ctx.getOptionValue("date");
						boolean enabled = ctx.getOptionValue("enabled");
						return String.format("Date: %s, Enabled: %s", date, enabled);
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regFileInfoCommand() {
		return CommandRegistration.builder()
				.command("reg-file-info")
				.description("Get file info")
				.withOption()
					.longNames("file")
					.type(File.class)
					.required(true)
					.description("Path to file")
					.and()
				.withTarget()
					.function(ctx -> {
						File file = ctx.getOptionValue("file");
						return String.format("File: %s, Exists: %s, Size: %d bytes",
								file.getAbsolutePath(),
								file.exists(),
								file.exists() ? file.length() : 0);
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regProcessIdsCommand() {
		return CommandRegistration.builder()
				.command("reg-process-ids")
				.description("Process multiple IDs")
				.withOption()
					.longNames("ids")
					.type(ResolvableType.forClassWithGenerics(List.class, ResolvableType.forClass(Integer.class)))
					.required(true)
					.description("List of IDs")
					.and()
				.withTarget()
					.function(ctx -> {
						List<Integer> ids = ctx.getOptionValue("ids");
						return "Processed IDs: " + ids.stream()
								.map(String::valueOf)
								.collect(Collectors.joining(", "));
					})
				.and()
				.build();
	}
}
```

#### 4.3.3. Вывод данных командой

Вывод осуществляется через возвращаемое значение функции.

```java
package ru.cu.springshelldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.CommandRegistration;

@Configuration
public class Example14TextOutputRegistration {

	@Bean
	public CommandRegistration regGreetCommand() {
		return CommandRegistration.builder()
				.command("reg-greet")
				.description("Greeting message")
				.withOption()
					.longNames("name")
					.shortNames('n')
					.type(String.class)
					.defaultValue("World")
					.description("Name to greet")
					.and()
				.withTarget()
					.function(ctx -> {
						String name = ctx.getOptionValue("name");
						return "Hello, " + name + "! Welcome to Spring Shell!";
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regMultilineCommand() {
		return CommandRegistration.builder()
				.command("reg-multiline")
				.description("Multiline output")
				.withTarget()
					.function(ctx -> "Line 1\nLine 2\nLine 3")
				.and()
				.build();
	}
}
```

```java
package ru.cu.springshelldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;

@Configuration
public class Example15TableOutputRegistration {

	@Bean
	public CommandRegistration regShowUsersCommand() {
		return CommandRegistration.builder()
				.command("reg-show-users")
				.description("Show users table")
				.withTarget()
					.function(ctx -> {
						TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

						modelBuilder.addRow()
								.addValue("ID")
								.addValue("Name")
								.addValue("Email")
								.addValue("Age");

						modelBuilder.addRow()
								.addValue("1")
								.addValue("Ivan Petrov")
								.addValue("ivan@example.com")
								.addValue("25");

						modelBuilder.addRow()
								.addValue("2")
								.addValue("Maria Sidorova")
								.addValue("maria@example.com")
								.addValue("30");

						modelBuilder.addRow()
								.addValue("3")
								.addValue("Alexey Smirnov")
								.addValue("alexey@example.com")
								.addValue("28");

						TableModel model = modelBuilder.build();
						Table table = new TableBuilder(model)
								.on(CellMatchers.column(0)).addAligner(SimpleHorizontalAligner.center).and()
								.on(CellMatchers.column(1)).addAligner(SimpleHorizontalAligner.left).and()
								.on(CellMatchers.column(2)).addAligner(SimpleHorizontalAligner.left).and()
								.on(CellMatchers.column(3)).addAligner(SimpleHorizontalAligner.center).and()
								.addFullBorder(BorderStyle.fancy_light)
								.build();

						return table;
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regShowProductsCommand() {
		return CommandRegistration.builder()
				.command("reg-show-products")
				.description("Show products table")
				.withTarget()
					.function(ctx -> {
						TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();

						modelBuilder.addRow().addValue("Product").addValue("Price").addValue("Stock");
						modelBuilder.addRow().addValue("Laptop").addValue("75000 ₽").addValue("15");
						modelBuilder.addRow().addValue("Mouse").addValue("2500 ₽").addValue("100");
						modelBuilder.addRow().addValue("Keyboard").addValue("4500 ₽").addValue("45");

						TableModel model = modelBuilder.build();
						Table table = new TableBuilder(model)
								.on(CellMatchers.column(0)).addAligner(SimpleHorizontalAligner.left).and()
								.on(CellMatchers.column(1)).addAligner(SimpleHorizontalAligner.right).and()
								.on(CellMatchers.column(2)).addAligner(SimpleHorizontalAligner.center).and()
								.addFullBorder(BorderStyle.fancy_light)
								.build();

						return table;
					})
				.and()
				.build();
	}
}
```

#### 4.3.4. Управление доступностью команд

Доступность задается через `.availability()` в цепочке билдера.

```java
package ru.cu.springshelldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.Availability;
import org.springframework.shell.command.CommandRegistration;

@Configuration
public class Example16AvailabilityRegistration {

	private boolean authenticated = false;
	private String currentUser = null;

	@Bean
	public CommandRegistration regLoginCommand() {
		return CommandRegistration.builder()
				.command("reg-login")
				.description("Login to system")
				.withOption()
					.longNames("username")
					.type(String.class)
					.required(true)
					.description("Username")
					.and()
				.withOption()
					.longNames("password")
					.type(String.class)
					.required(true)
					.description("Password")
					.and()
				.withTarget()
					.function(ctx -> {
						String username = ctx.getOptionValue("username");
						String password = ctx.getOptionValue("password");

						if ("admin".equals(username) && "secret".equals(password)) {
							authenticated = true;
							currentUser = username;
							return "Login successful! Welcome, " + username;
						}
						return "Login failed! Invalid credentials.";
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regLogoutCommand() {
		return CommandRegistration.builder()
				.command("reg-logout")
				.description("Logout from system")
				.withTarget()
					.function(ctx -> {
						if (authenticated) {
							authenticated = false;
							currentUser = null;
							return "Logout successful!";
						}
						return "You are not logged in.";
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regSecretCommand() {
		return CommandRegistration.builder()
				.command("reg-secret")
				.description("Secret command")
				.availability(() -> authenticated
						? Availability.available()
						: Availability.unavailable("You need to login first. Use 'reg-login' command."))
				.withTarget()
					.function(ctx -> "This is secret information!")
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regStatusCommand() {
		return CommandRegistration.builder()
				.command("reg-status")
				.description("Show status")
				.withTarget()
					.function(ctx -> authenticated
							? "Authenticated as: " + currentUser
							: "Not authenticated")
				.and()
				.build();
	}
}
```
---
## 5. Интерактивный и неинтерактивный режим Shell

### 5.1. Общая концепция режимов работы

Spring Shell поддерживает три режима работы приложения, которые определяют способ взаимодействия с пользователем:

| Режим | Описание | Сценарий использования |
|-------|----------|------------------------|
| **Интерактивный** | REPL-цикл: пользователь вводит команды, получает ответы, цикл повторяется | Административные консоли, инструменты разработчика, интерактивная работа |
| **Неинтерактивный** | Приложение выполняет одну команду и завершается | Скрипты, CI/CD пайплайны, однократные вызовы |
| **Скриптовый** | Выполнение команд из файла (скрипта) | Автоматизация, пакетная обработка, воспроизведение сценариев |

### 5.2. Настройка режимов

Режимы работы настраиваются в `application.yml` или через системные свойства.

**Ключевые настройки:**
- `spring.shell.interactive.enabled` - включение интерактивного режима (по умолчанию `true`)
- `spring.shell.noninteractive.enabled` - включение неинтерактивного режима (по умолчанию `true` с версии 3.3.x)
- `spring.shell.script.enabled` - включение скриптового режима

### 5.3. Интерактивный режим

#### 5.3.1. Особенности интерактивного режима

При запуске приложения с включенным интерактивным режимом:

1. Загружается Spring-контекст
2. Выводится приветственное сообщение (если настроено)
3. Появляется приглашение `shell:>`
4. Пользователь может вводить команды
5. История команд сохраняется
6. Команда `quit` или `exit` завершает приложение

#### 5.3.2. Пример работы в интерактивном режиме

```text
$ java -jar spring-shell-demo-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.5.12)

shell:>help

AVAILABLE COMMANDS

Built-in Commands
        help: Display help about available commands
        quit: Exit the shell
        clear: Clear the shell screen
        history: Display or save the history

User Commands (programmatic)
        reg-user-info: Display user information

shell:>hello
Hello from Spring Shell!

shell:>user-info --name Ivan --age 25 --email ivan@example.com
User: Ivan, Age: 25, Email: ivan@example.com

shell:>quit
```

### 5.4. Неинтерактивный режим

#### 5.4.1. Особенности неинтерактивного режима

При запуске приложения с включенным неинтерактивным режимом:

1. Загружается Spring-контекст
2. Выполняется команда, переданная в аргументах командной строки
3. Приложение завершается после выполнения команды
4. Код возврата отражает успешность выполнения

#### 5.4.2. Запуск в неинтерактивном режиме

```text
java -jar spring-shell-demo-0.0.1-SNAPSHOT.jar --command "hello"
```

#### 5.4.3. Пример работы в неинтерактивном режиме

```text
# Выполнение одной команды
$ java -jar spring-shell-demo-0.0.1-SNAPSHOT.jar --command "hello"
Hello from Spring Shell!

# Выполнение команды с параметрами
$ java -jar spring-shell-demo-0.0.1-SNAPSHOT.jar --command "user-info --name Ivan --age 25 --email ivan@example.com"
User: Ivan, Age: 25, Email: ivan@example.com
```

### 5.5. Скриптовый режим
#### 5.5.1. Особенности скриптового режима
Скриптовый режим позволяет выполнять последовательность команд из файла:
1. Каждая строка файла интерпретируется как команда
2. Поддерживаются комментарии (строки, начинающиеся с `#`)
3. Можно использовать пустые строки
4. Вывод команд сохраняется в консоль или перенаправляется
#### 5.5.2. Пример скриптового файла

```text
hello

user-info --name Admin --age 30 --email admin@example.com

show-users
```

#### 5.5.3. Запуск скрипта

```text
$ java -jar spring-shell-demo-0.0.1-SNAPSHOT.jar --script --file "scripts/demo-script.txt"
```
или
```text
shell:>script --file "scripts/demo-script.txt"
```

### 5.6. Определение режима в коде
В коде команды можно определить, в каком режиме выполняется приложение, и адаптировать поведение.

Пример
```java
package ru.cu.springshelldemo.command;

import org.springframework.core.env.Environment;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.context.InteractionMode;
import org.springframework.shell.context.ShellContext;

@Command
public class Example17ModeAwareCommand {

	private final ShellContext shellContext;
	private final Environment environment;

	public Example17ModeAwareCommand(ShellContext shellContext, Environment environment) {
		this.shellContext = shellContext;
		this.environment = environment;
	}

	@Command(command = "mode-info", description = "Show current execution mode")
	public String modeInfo() {
		InteractionMode im = shellContext.getInteractionMode();
		String modeLine;
		if (im == InteractionMode.INTERACTIVE) {
			modeLine = "INTERACTIVE (REPL mode)";
		}
		else if (im == InteractionMode.NONINTERACTIVE) {
			modeLine = "NON-INTERACTIVE (single command)";
		}
		else {
			modeLine = im.name();
		}
		return String.format(
				"Current execution mode: %s\n" +
						"Interactive enabled: %s\n" +
						"Script enabled: %s\n" +
						"Non-interactive enabled: %s",
				modeLine,
				environment.getProperty("spring.shell.interactive.enabled", Boolean.class, true),
				environment.getProperty("spring.shell.script.enabled", Boolean.class, true),
				environment.getProperty("spring.shell.noninteractive.enabled", Boolean.class, true));
	}

	@Command(command = "smart-greet", description = "Smart greeting based on mode")
	public String smartGreet(
			@Option(longNames = "name", description = "Name", defaultValue = "User") String name) {
		InteractionMode im = shellContext.getInteractionMode();
		if (im == InteractionMode.INTERACTIVE) {
			return String.format("Hello %s! You are in interactive mode. Type 'help' for commands.", name);
		}
		if (im == InteractionMode.NONINTERACTIVE) {
			return String.format("Hello %s! Single command execution.", name);
		}
		return String.format("Hello %s! Shell interaction mode: %s.", name, im);
	}
}
```

Использование
```bash
# В интерактивном режиме
shell:>mode-info
Current execution mode: INTERACTIVE (REPL mode)
Interactive enabled: true
Script enabled: true
Non-interactive enabled: true

# В неинтерактивном режиме
$ java -jar spring-shell-demo-0.0.1-SNAPSHOT.jar mode-info
Current execution mode: NON-INTERACTIVE (single command)
Interactive enabled: true
Script enabled: true
Non-interactive enabled: true

# В интерактивном режиме
shell:>smart-greet --name User
Hello User! You are in interactive mode. Type 'help' for commands.

# В неинтерактивном режиме
$ java -jar spring-shell-demo-0.0.1-SNAPSHOT.jar smart-greet --name User
Hello User! Single command execution.

```

