package ru.cu.lifecycle;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class CustomBeanDefinitionRegistrar extends LifeCycleStepBase implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata importingClassMetadata,
                                        @Nonnull BeanDefinitionRegistry registry,
                                        @Nonnull BeanNameGenerator importBeanNameGenerator) {
        registerBeanDefinitions(importingClassMetadata, registry);
    }

    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata metadata,
                                        @Nonnull BeanDefinitionRegistry registry) {

        if (printStepIfNecessary("#0: ImportBeanDefinitionRegistrar.registerBeanDefinitions\n")) {
            registerAwareWorkerBean(registry);
        }
    }

    private void registerAwareWorkerBean(BeanDefinitionRegistry registry) {
        GenericBeanDefinition gbd = new GenericBeanDefinition();
        gbd.setBeanClass(AwareInterfacesAndCallbacksSubscriberBean.class);
        gbd.setInitMethodName("customInitMethod");
        gbd.setDestroyMethodName("customDestroyMethod");
        registry.registerBeanDefinition("subscriberBean", gbd);
    }
}
