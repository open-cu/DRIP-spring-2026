package ru.cu.custom.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cu.custom.scope.JobActionScope;

import java.util.List;
import java.util.function.Supplier;

@Configuration
public class ActionsConfig {

    @JobActionScope
    @Bean
    public JobAction calculateSumAction(@Value("#{jobParams['integers']}") List<Integer> integers) {
        return new JobAction() {
            @Override
            public void exec() {
                var result = integers.stream()
                        .reduce(0, Integer::sum);
                System.out.printf("Сумма чисел %s равна %d%n", integers, result);
            }
        };
    }

    @JobActionScope
    @Bean
    public JobAction execLambdaAction(@Value("#{jobParams['lambda']}") Supplier<String> lambda) {
        return new JobAction() {
            @Override
            public void exec() {

                System.out.printf("Результат лямбды равен %s%n", lambda.get());
            }
        };
    }

    @JobActionScope
    @Bean
    public JobAction fireEventAction(ApplicationEventPublisher eventPublisher,
                                     @Value("#{jobParams['eventName']}") String eventName) {
        return new JobAction() {
            @Override
            public void exec() {
                eventPublisher.publishEvent(new CustomEvent(this, eventName));
                System.out.println("Событие отправлено");
            }
        };
    }
}
