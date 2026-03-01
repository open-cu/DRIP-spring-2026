package ru.cu.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BaseScopesDemo {
    public static void main(String[] args) {
        SpringApplication.run(BaseScopesDemo.class);
        System.out.println("http://localhost:8080/api/self-info");
    }
}
