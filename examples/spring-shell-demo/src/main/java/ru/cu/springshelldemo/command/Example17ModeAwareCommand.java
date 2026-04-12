package ru.cu.springshelldemo.command;

import org.springframework.core.env.Environment;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.context.InteractionMode;
import org.springframework.shell.context.ShellContext;

@Command
public class Example17ModeAwareCommand {

	private final ShellContext shellContext;
	private final Environment environment;

	public Example17ModeAwareCommand(ShellContext shellContext, Environment environment) {
		this.shellContext = shellContext;
		this.environment = environment;
	}

	@Command(command = "mode-info", description = "Show current execution mode")
	public String modeInfo() {
		InteractionMode im = shellContext.getInteractionMode();
		String modeLine;
		if (im == InteractionMode.INTERACTIVE) {
			modeLine = "INTERACTIVE (REPL mode)";
		}
		else if (im == InteractionMode.NONINTERACTIVE) {
			modeLine = "NON-INTERACTIVE (single command)";
		}
		else {
			modeLine = im.name();
		}
		return String.format(
				"Current execution mode: %s\n" +
						"Interactive enabled: %s\n" +
						"Script enabled: %s\n" +
						"Non-interactive enabled: %s",
				modeLine,
				environment.getProperty("spring.shell.interactive.enabled", Boolean.class, true),
				environment.getProperty("spring.shell.script.enabled", Boolean.class, true),
				environment.getProperty("spring.shell.noninteractive.enabled", Boolean.class, true));
	}

	@Command(command = "smart-greet", description = "Smart greeting based on mode")
	public String smartGreet(
			@Option(longNames = "name", description = "Name", defaultValue = "User") String name) {
		InteractionMode im = shellContext.getInteractionMode();
		if (im == InteractionMode.INTERACTIVE) {
			return String.format("Hello %s! You are in interactive mode. Type 'help' for commands.", name);
		}
		if (im == InteractionMode.NONINTERACTIVE) {
			return String.format("Hello %s! Single command execution.", name);
		}
		return String.format("Hello %s! Shell interaction mode: %s.", name, im);
	}
}
