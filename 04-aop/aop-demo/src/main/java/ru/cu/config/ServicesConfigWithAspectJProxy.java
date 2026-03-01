package ru.cu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.cu.config.aspect.BenchmarkAspect;
import ru.cu.services.ActionsService;
import ru.cu.services.ActionsServiceImpl;

// 02
@Import(BenchmarkAspect.class)
@Configuration
public class ServicesConfigWithAspectJProxy {

    @Bean
    public ActionsService actionsService1() {
        return new ActionsServiceImpl();
    }

    @Bean
    public ActionsService actionsService2() {
        return new ActionsServiceImpl();
    }
}
