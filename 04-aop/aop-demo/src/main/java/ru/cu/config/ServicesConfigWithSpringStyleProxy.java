package ru.cu.config;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cu.config.aspect.Benchmarkable;
import ru.cu.services.ActionsService;
import ru.cu.services.ActionsServiceImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static ru.cu.utils.BenchmarkUtils.execWithBenchmark;

//01
@Configuration
public class ServicesConfigWithSpringStyleProxy {

    @Bean
    public ActionsService actionsService1() {
        var originalActionsService = new ActionsServiceImpl();
        var handler = new java.lang.reflect.InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] methodArgs) throws Throwable {
                var originalMethod = originalActionsService.getClass().getMethod(method.getName(), method.getParameterTypes());
                if (!originalMethod.isAnnotationPresent(Benchmarkable.class)) {
                    return method.invoke(originalActionsService);
                }
                return execWithBenchmark(originalActionsService, method, methodArgs);
            }
        };
        return (ActionsService) Proxy.newProxyInstance(ActionsService.class.getClassLoader(),
                new Class<?>[]{ActionsService.class},
                handler);
    }

    @Bean
    public ActionsService actionsService2() {
        var originalActionsService = new ActionsServiceImpl();
        var handler = new org.springframework.cglib.proxy.InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] methodArgs) throws Throwable {
                if (!method.isAnnotationPresent(Benchmarkable.class)) {
                    return method.invoke(originalActionsService);
                }
                return execWithBenchmark(originalActionsService, method, methodArgs);
            }
        };
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ActionsServiceImpl.class);
        enhancer.setCallback(handler);
        return (ActionsService) enhancer.create();
    }

}
