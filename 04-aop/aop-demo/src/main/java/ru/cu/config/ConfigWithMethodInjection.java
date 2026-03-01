package ru.cu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cu.services.InformationService;
import ru.cu.services.ActionsService;
import ru.cu.services.ActionsServiceImpl;

// 03
@Configuration
public class ConfigWithMethodInjection {

    @Bean
    public ActionsService actionsService() {
        return new ActionsServiceImpl();
    }

    @Bean
    public InformationService informationService1() {
        return new InformationService(actionsService());
    }

    @Bean
    public InformationService informationService2() {
        return new InformationService(actionsService());
    }

    @Bean
    public InformationService informationService3() {
        return new InformationService(actionsService());
    }
}
