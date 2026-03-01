package ru.cu.lifecycle;

import ru.cu.service.ActualImportantServiceBean;
import ru.cu.service.DeprecatedImportantServiceBean;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

public class CustomBeanFactoryPostProcessor extends LifeCycleStepBase implements BeanFactoryPostProcessor {

    private static final String CLASS_NAME_ATTR = "className";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        printStepIfNecessary("#1: BeanFactoryPostProcessor.postProcessBeanFactory\n");
        replaceWorkerBeanClass(beanFactory);
    }

    private void replaceWorkerBeanClass(ConfigurableListableBeanFactory beanFactory) {
        if (isPropertyTrue("use.deprecated.service.enabled")) {
            return;
        }

        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            var d = beanFactory.getBeanDefinition(beanName);
            if (d instanceof ScannedGenericBeanDefinition scannedBeanDefinition) {

                if (!DeprecatedImportantServiceBean.class.getName().equalsIgnoreCase(d.getBeanClassName())) {
                    continue;
                }
                d.setBeanClassName(ActualImportantServiceBean.class.getName());
                var classNameAttr = new BeanMetadataAttribute(CLASS_NAME_ATTR, ActualImportantServiceBean.class.getName());
                scannedBeanDefinition.addMetadataAttribute(classNameAttr);
            }
        }
    }
}
