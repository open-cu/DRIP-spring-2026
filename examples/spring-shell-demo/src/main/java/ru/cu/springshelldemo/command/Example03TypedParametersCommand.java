package ru.cu.springshelldemo.command;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class Example03TypedParametersCommand {

	@ShellMethod(key = "calc", value = "Perform calculations")
	public String calculate(
			@ShellOption(help = "First number") int a,
			@ShellOption(help = "Second number") double b,
			@ShellOption(help = "Multiplier") BigDecimal multiplier) {
		BigDecimal result = BigDecimal.valueOf(a + b).multiply(multiplier);
		return String.format("Result: %s", result);
	}

	@ShellMethod(key = "date-info", value = "Process date")
	public String dateInfo(
			@ShellOption(help = "Some date") LocalDate date,
			@ShellOption(help = "Enable flag") boolean enabled) {
		return String.format("Date: %s, Enabled: %s", date, enabled);
	}

	@ShellMethod(key = "file-info", value = "Get file info")
	public String fileInfo(
			@ShellOption(help = "Path to file") File file) {
		return String.format("File: %s, Exists: %s, Size: %d bytes",
				file.getAbsolutePath(),
				file.exists(),
				file.exists() ? file.length() : 0);
	}
}
