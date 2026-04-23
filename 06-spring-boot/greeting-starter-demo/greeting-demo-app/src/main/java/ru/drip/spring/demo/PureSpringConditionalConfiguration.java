package ru.drip.spring.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PureSpringConditionalConfiguration {

    @Bean
    @Conditional(ExperimentalFeatureEnabledCondition.class)
    public ExperimentalFeature experimentalFeature() {
        return () -> "[EXPERIMENTAL] включено через чистый Spring @Conditional";
    }
}

