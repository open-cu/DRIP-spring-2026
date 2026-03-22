package ru.cu.springshelldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.AvailabilityProvider;

import ru.cu.springshelldemo.command.Example11ModernAvailabilityCommand;

@Configuration
public class Example18ModernAvailabilityConfig {

	@Bean
	public AvailabilityProvider mSecretAvailabilityProvider(Example11ModernAvailabilityCommand modernAvailabilityCommand) {
		return modernAvailabilityCommand::evaluateSecretAvailability;
	}
}
