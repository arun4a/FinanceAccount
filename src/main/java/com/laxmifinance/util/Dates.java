package com.laxmifinance.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Dates {

    public static final DateTimeFormatter DB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Dates() {
    }

    public static String defaultStartDate() {
        return LocalDate.now().minusDays(3).format(DB_FORMAT);
    }

    public static String defaultEndDate() {
        return LocalDate.now().format(DB_FORMAT);
    }

    public static String orDefaultStart(String value) {
        return isValid(value) ? value : defaultStartDate();
    }

    public static String orDefaultEnd(String value) {
        return isValid(value) ? value : defaultEndDate();
    }

    public static String today() {
        return LocalDate.now().format(DB_FORMAT);
    }

    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            LocalDate.parse(value, DB_FORMAT);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static String formatDb(LocalDate date) {
        return date.format(DB_FORMAT);
    }
}
