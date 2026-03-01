package ru.cu;

import ru.cu.service.TargetService;

/*
Запуск примера:
    1. В IDEA, вменю запуска выбираем "Edit Configurations..."
    2. В поле "VM options" вносим "-javaagent:${путь.до.m2}\.m2\repository\org\aspectj\aspectjweaver\
            {версия.библиотеки}\aspectjweaver-{версия.библиотеки}.jar",
       где ${путь.до.m2} это путь до репозитория maven на текущем компьютере.
       Пример: "-javaagent:c:\mvnrep\org\aspectj\aspectjweaver\1.9.19\aspectjweaver-1.9.19.jar".
    3. Запускаем Main
*/

public class Main {

    public static void main(String[] args) {
        var service = new TargetService();
        service.doAction("any string");
    }
}
