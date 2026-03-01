package ru.cu.lifecycle;

import ru.cu.service.ImportantService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@RequiredArgsConstructor
public class CustomBeanPostProcessor extends LifeCycleStepBase implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(@Nonnull Object bean,
                                                  @Nonnull String beanName) throws BeansException {
        if (isSubscriberBean(beanName)) {
            printStepIfNecessary("#5: BeanPostProcessor.postProcessBeforeInitialization\n");
        }
        return wrapInProxyIfNecessary(bean);
    }

    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean,
                                                 @Nonnull String beanName) throws BeansException {
        if (isSubscriberBean(beanName)) {
            printStepIfNecessary("#9: BeanPostProcessor.postProcessAfterInitialization\n");
        }
        return bean;
    }

    private boolean isSubscriberBean(String beanName) {
        return "subscriberBean".equals(beanName);
    }

    private Object wrapInProxyIfNecessary(Object bean) {
        if (!ImportantService.class.isAssignableFrom(bean.getClass()) || isStepsPrintingRegimeEnabled() ||
                isPropertyFalse("logging.proxy.enabled")) {
            return bean;
        }

        var invocationHandler = (InvocationHandler) (proxy, method, args) -> {
            System.out.println("Начал делать работу...");
            try {
                return method.invoke(bean, args);
            } finally {
                System.out.println("Закончил делать работу...");
            }
        };

        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{ImportantService.class}, invocationHandler);
    }
}
