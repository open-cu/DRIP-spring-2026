package ru.cu.springshelldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.EnableCommand;

import ru.cu.springshelldemo.command.Example07ModernUserCommand;
import ru.cu.springshelldemo.command.Example08ModernTypedParametersCommand;
import ru.cu.springshelldemo.command.Example09ModernTextOutputCommand;
import ru.cu.springshelldemo.command.Example10ModernTableOutputCommand;
import ru.cu.springshelldemo.command.Example11ModernAvailabilityCommand;
import ru.cu.springshelldemo.command.Example17ModeAwareCommand;

@SpringBootApplication
@EnableCommand({
		Example11ModernAvailabilityCommand.class,
		Example10ModernTableOutputCommand.class,
		Example09ModernTextOutputCommand.class,
		Example08ModernTypedParametersCommand.class,
		Example07ModernUserCommand.class,
		Example17ModeAwareCommand.class
})
public class SpringShellDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringShellDemoApplication.class, args);
	}
}
