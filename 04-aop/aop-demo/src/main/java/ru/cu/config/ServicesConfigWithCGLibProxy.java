package ru.cu.config;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cu.config.aspect.Benchmarkable;
import ru.cu.services.ActionsService;
import ru.cu.services.ActionsServiceImpl;

import java.lang.reflect.Method;

import static ru.cu.utils.BenchmarkUtils.execWithBenchmark;

// 04
@Configuration
public class ServicesConfigWithCGLibProxy {

    @Bean
    public ActionsService actionsService1() {
        var handler = new org.springframework.cglib.proxy.MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                if (!method.isAnnotationPresent(Benchmarkable.class)) {
                    return methodProxy.invokeSuper(obj, args);
                }
                return execWithBenchmark(obj, methodProxy, args);
            }
        };
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ActionsServiceImpl.class);
        enhancer.setCallback(handler);
        return (ActionsService) enhancer.create();
    }

    @Bean
    public ActionsService actionsService2() {
        var handler = new org.springframework.cglib.proxy.MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                if (!method.isAnnotationPresent(Benchmarkable.class)) {
                    return methodProxy.invokeSuper(obj, args);
                }
                return execWithBenchmark(obj, methodProxy, args);
            }
        };
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ActionsServiceImpl.class);
        enhancer.setCallback(handler);
        return (ActionsService) enhancer.create();
    }
}
