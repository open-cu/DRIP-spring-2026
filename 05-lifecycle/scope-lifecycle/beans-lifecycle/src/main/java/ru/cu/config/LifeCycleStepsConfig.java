package ru.cu.config;

import ru.cu.lifecycle.CustomBeanDefinitionRegistrar;
import ru.cu.lifecycle.CustomBeanFactoryPostProcessor;
import ru.cu.lifecycle.CustomBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({CustomBeanDefinitionRegistrar.class, CustomBeanFactoryPostProcessor.class,
        CustomBeanPostProcessor.class})
@Configuration
public class LifeCycleStepsConfig {
}
