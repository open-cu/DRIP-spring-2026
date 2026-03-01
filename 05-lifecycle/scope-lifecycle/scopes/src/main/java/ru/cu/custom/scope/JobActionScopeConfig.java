package ru.cu.custom.scope;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static ru.cu.custom.scope.JobActionScopeImpl.SCOPE_NAME;

@Configuration
public class JobActionScopeConfig {
    @Bean
    public static CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.setScopes(Map.of(SCOPE_NAME, new JobActionScopeImpl()));
        return configurer;
    }
}
