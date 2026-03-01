package ru.cu.custom.scope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import static ru.cu.custom.scope.JobActionScopeImpl.SCOPE_NAME;

//@Component
public class JobActionScopeBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        var scope = new JobActionScopeImpl();
        beanFactory.registerScope(SCOPE_NAME, scope);
    }
}
