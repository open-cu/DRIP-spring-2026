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
