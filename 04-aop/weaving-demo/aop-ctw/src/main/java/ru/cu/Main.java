package ru.cu;

import ru.cu.service.TargetService;

/*
Запуск примера:
    1. cd weaving-demo/aop-ctw/
    2. mvn clean package
    3. java -jar target/aop-ctw-1.0-jar-with-dependencies.jar
 */

public class Main {

    public static void main(String[] args) {
        var service = new TargetService();
        service.doAction("any string");
    }
}
