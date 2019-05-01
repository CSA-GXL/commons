package com.terran4j.commons.api2doc.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

public class DateConverter {

    public static boolean isDateType(Class<?> clazz) {
        return Arrays.asList(Date.class , java.sql.Date.class,LocalDateTime.class,LocalDate.class).contains(clazz);
    }

    public static Object dateAsLongValue(Class<?> clazz) {
        if (isDateType(clazz)) {
            // 这里不能用 Long 类型，因为赋值会失败。
            return new Date();
        }
        return null;
    }

    public static ApiDataType dateAsLongType(Class<?> clazz) {
        if (isDateType(clazz)) {
            return ApiDataType.LONG;
        }
        return null;
    }

    public static Class<?> dateAsLongClass(Class<?> clazz) {
        if (isDateType(clazz)) {
            return Long.class;
        }
        return clazz;
    }
}
