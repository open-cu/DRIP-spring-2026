package ru.cu.services;

import ru.cu.config.aspect.Benchmarkable;

import static ru.cu.utils.SleepUtils.delay;

public class ActionsServiceImpl implements ActionsService {

    @Benchmarkable
    @Override
    public void doFastAction() {
        System.out.println("doFastAction");
        delay(700);
    }

    @Benchmarkable
    @Override
    public void doSlowAction() {
        System.out.println("doSlowAction");
        delay(300);
        doFastAction();
    }

}
