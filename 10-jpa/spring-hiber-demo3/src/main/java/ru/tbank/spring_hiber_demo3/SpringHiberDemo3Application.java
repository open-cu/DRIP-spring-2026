package ru.tbank.spring_hiber_demo3;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class SpringHiberDemo3Application {

    public static void main(String[] args) throws SQLException {
		SpringApplication.run(SpringHiberDemo3Application.class, args);
		Console.main(args);
	}
}
