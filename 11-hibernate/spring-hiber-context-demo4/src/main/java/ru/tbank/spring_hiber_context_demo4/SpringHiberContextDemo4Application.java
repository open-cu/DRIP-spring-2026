package ru.tbank.spring_hiber_context_demo4;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class SpringHiberContextDemo4Application {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(SpringHiberContextDemo4Application.class, args);
		Console.main(args);
	}

}
