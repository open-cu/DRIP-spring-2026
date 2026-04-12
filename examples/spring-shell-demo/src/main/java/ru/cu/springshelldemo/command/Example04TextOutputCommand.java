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
