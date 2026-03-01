package ru.cu.utils;

public class SleepUtils {

    public static void delay(long mills){
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
