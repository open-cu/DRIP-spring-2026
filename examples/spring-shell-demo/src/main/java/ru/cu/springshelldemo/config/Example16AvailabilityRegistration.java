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
