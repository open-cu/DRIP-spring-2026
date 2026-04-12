package ru.cu.springshelldemo.command;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command
public class Example08ModernTypedParametersCommand {

	@Command(command = "m-calc", description = "Perform calculations")
	public String calculate(
			@Option(longNames = "a", description = "First number", required = true) int a,
			@Option(longNames = "b", description = "Second number", required = true) double b,
			@Option(longNames = "multiplier", description = "Multiplier", required = true) BigDecimal multiplier) {
		BigDecimal result = BigDecimal.valueOf(a + b).multiply(multiplier);
		return String.format("Result: %s", result);
	}

	@Command(command = "m-date-info", description = "Process date")
	public String dateInfo(
			@Option(longNames = "date", description = "Some date", required = true) LocalDate date,
			@Option(longNames = "enabled", description = "Enable flag", defaultValue = "false") boolean enabled) {
		return String.format("Date: %s, Enabled: %s", date, enabled);
	}

	@Command(command = "m-file-info", description = "Get file info")
	public String fileInfo(
			@Option(longNames = "file", description = "Path to file", required = true) File file) {
		return String.format("File: %s, Exists: %s, Size: %d bytes",
				file.getAbsolutePath(),
				file.exists(),
				file.exists() ? file.length() : 0);
	}

	@Command(command = "m-process-ids", description = "Process multiple IDs")
	public String processIds(
			@Option(longNames = "ids", description = "List of IDs", required = true) List<Integer> ids) {
		return "Processed IDs: " + ids.stream().map(String::valueOf).collect(Collectors.joining(", "));
	}
}
