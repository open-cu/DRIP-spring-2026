package ru.cu.springshelldemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.shell.interactive.enabled=false")
class SpringShellDemoApplicationTests {

	@Test
	void contextLoads() {
	}
}
