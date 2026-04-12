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
