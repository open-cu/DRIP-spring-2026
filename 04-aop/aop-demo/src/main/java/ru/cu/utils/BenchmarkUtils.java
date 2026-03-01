package ru.cu.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class BenchmarkUtils {

    public static Object execWithBenchmark(Supplier<Object> action) {
        long t = System.currentTimeMillis();
        try {
            return action.get();
        } catch (Throwable e){
            throw new RuntimeException(e);
        } finally {
            t = System.currentTimeMillis() - t;
            System.out.printf("Execution time is %d ms%n", t);
        }
    }

    public static Object execWithBenchmark(ProceedingJoinPoint joinPoint) {
        return execWithBenchmark(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e){
                throw new RuntimeException(e);
            }
        });
    }

    public static Object execWithBenchmark(Object object, Method method, Object[] methodArgs) {
        return execWithBenchmark(() -> {
            try {
                return method.invoke(object, methodArgs);
            } catch (Throwable e){
                throw new RuntimeException(e);
            }
        });
    }

    public static Object execWithBenchmark(Object object, MethodProxy methodProxy, Object[] methodArgs) {
        return execWithBenchmark(() -> {
            try {
                return methodProxy.invokeSuper(object, methodArgs);
            } catch (Throwable e){
                throw new RuntimeException(e);
            }
        });
    }
}
