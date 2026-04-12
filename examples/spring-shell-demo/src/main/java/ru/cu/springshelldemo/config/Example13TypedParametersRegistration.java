package ru.cu.springshelldemo.config;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.shell.command.CommandRegistration;

@Configuration
public class Example13TypedParametersRegistration {

	@Bean
	public CommandRegistration regCalculateCommand() {
		return CommandRegistration.builder()
				.command("reg-calc")
				.description("Perform calculations")
				.withOption()
					.longNames("a")
					.type(Integer.class)
					.required(true)
					.description("First number")
					.and()
				.withOption()
					.longNames("b")
					.type(Double.class)
					.required(true)
					.description("Second number")
					.and()
				.withOption()
					.longNames("multiplier")
					.type(BigDecimal.class)
					.required(true)
					.description("Multiplier")
					.and()
				.withTarget()
					.function(ctx -> {
						int a = ctx.getOptionValue("a");
						double b = ctx.getOptionValue("b");
						BigDecimal multiplier = ctx.getOptionValue("multiplier");
						BigDecimal result = BigDecimal.valueOf(a + b).multiply(multiplier);
						return String.format("Result: %s", result);
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regDateInfoCommand() {
		return CommandRegistration.builder()
				.command("reg-date-info")
				.description("Process date")
				.withOption()
					.longNames("date")
					.type(LocalDate.class)
					.required(true)
					.description("Some date")
					.and()
				.withOption()
					.longNames("enabled")
					.type(Boolean.class)
					.defaultValue("false")
					.description("Enable flag")
					.and()
				.withTarget()
					.function(ctx -> {
						LocalDate date = ctx.getOptionValue("date");
						boolean enabled = ctx.getOptionValue("enabled");
						return String.format("Date: %s, Enabled: %s", date, enabled);
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regFileInfoCommand() {
		return CommandRegistration.builder()
				.command("reg-file-info")
				.description("Get file info")
				.withOption()
					.longNames("file")
					.type(File.class)
					.required(true)
					.description("Path to file")
					.and()
				.withTarget()
					.function(ctx -> {
						File file = ctx.getOptionValue("file");
						return String.format("File: %s, Exists: %s, Size: %d bytes",
								file.getAbsolutePath(),
								file.exists(),
								file.exists() ? file.length() : 0);
					})
				.and()
				.build();
	}

	@Bean
	public CommandRegistration regProcessIdsCommand() {
		return CommandRegistration.builder()
				.command("reg-process-ids")
				.description("Process multiple IDs")
				.withOption()
					.longNames("ids")
					.type(ResolvableType.forClassWithGenerics(List.class, ResolvableType.forClass(Integer.class)))
					.required(true)
					.description("List of IDs")
					.and()
				.withTarget()
					.function(ctx -> {
						List<Integer> ids = ctx.getOptionValue("ids");
						return "Processed IDs: " + ids.stream()
								.map(String::valueOf)
								.collect(Collectors.joining(", "));
					})
				.and()
				.build();
	}
}
