package ru.cu.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConversionUtils {
    public static String formatDate(LocalDate date, String format) {
        if (date == null || format == null || format.isEmpty()) {
            throw new IllegalArgumentException("Date and format must not be null or empty");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }
}
