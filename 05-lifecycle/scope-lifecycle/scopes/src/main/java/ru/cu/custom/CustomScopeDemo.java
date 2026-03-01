package ru.cu.custom;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.cu.custom.app.JobActionsRunner;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@ComponentScan
public class CustomScopeDemo {

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(CustomScopeDemo.class);

        var runner = ctx.getBean(JobActionsRunner.class);
        runner.runJobAction("calculateSumAction",
                Map.of("integers", List.of(1, 2, 3, 4, 5)));

        Supplier<String> lambda = () -> "Helo World!!!";
        runner.runJobAction("execLambdaAction",
                Map.of("lambda", lambda));

        runner.runJobAction("fireEventAction",
                Map.of("eventName", "В лесу упало дерево"));

    }
}
