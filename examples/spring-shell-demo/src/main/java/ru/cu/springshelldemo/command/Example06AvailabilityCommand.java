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
