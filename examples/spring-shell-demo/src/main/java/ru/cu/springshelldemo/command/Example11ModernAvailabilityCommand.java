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
