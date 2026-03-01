package ru.cu.lifecycle;

import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class AwareInterfacesAndCallbacksSubscriberBean implements InitializingBean, DisposableBean, BeanNameAware,
        BeanFactoryAware, ApplicationContextAware {

    @Override
    public void setBeanName(@Nonnull String s) {
        System.out.println("#2: BeanNameAware\n");
    }

    @Override
    public void setBeanFactory(@Nonnull BeanFactory beanFactory) throws BeansException {
        System.out.println("#3: BeanFactoryAware\n");
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        System.out.println("#4: ApplicationContextAware\n");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("#6: @PostConstruct\n");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("#7: InitializingBean.afterPropertiesSet\n");
    }

    public void customInitMethod() {
        System.out.println("#8: CustomLifeCycleBean.customInitMethod\n");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("#10: @PreDestroy\n");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("#11: DisposableBean.destroy\n");
    }

    public void customDestroyMethod() {
        System.out.println("#12: CustomLifeCycleBean.customDestroyMethod\n");
    }
}
