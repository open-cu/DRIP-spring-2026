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
