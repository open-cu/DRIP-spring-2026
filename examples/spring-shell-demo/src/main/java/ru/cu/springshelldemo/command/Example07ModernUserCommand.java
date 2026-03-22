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
