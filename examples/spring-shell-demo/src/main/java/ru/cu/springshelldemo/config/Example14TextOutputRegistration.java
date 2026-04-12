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
