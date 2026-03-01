package ru.cu.helper;

import static java.util.Objects.nonNull;

public class DemoUtils {

    public static void showValueWithRealAndExpectedTypes(Object val, Class<?> clazz) {
        if (nonNull(val)) {
            System.out.printf("Type %s: %s%n", clazz.getSimpleName(), clazz.isAssignableFrom(val.getClass()));
        }
        System.out.printf("Value: %s%n%n", val);
    }
}
