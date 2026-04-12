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
