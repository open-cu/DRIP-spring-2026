package ru.cu.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import static ru.cu.utils.BenchmarkUtils.execWithBenchmark;

@Aspect
public class BenchmarkAspect {

    @Around(value = "@annotation(ru.cu.config.aspect.Benchmarkable)")
    public Object benchmark(ProceedingJoinPoint joinPoint) {
        return execWithBenchmark(joinPoint);
    }
}
