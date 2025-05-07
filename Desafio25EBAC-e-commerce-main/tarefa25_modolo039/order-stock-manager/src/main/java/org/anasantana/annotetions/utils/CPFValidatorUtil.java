package org.anasantana.annotetions.utils;

import org.anasantana.annotetions.CPF;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CPFValidatorUtil {

    private static final Pattern CPF_PATTERN = Pattern.compile(
        "^(\\d{3})[\\s.-]?(\\d{3})[\\s.-]?(\\d{3})[\\s.-]?(\\d{2})$"
    );

    public static void validateAndFormatCPF(Object entity) {
        Class<?> clazz = entity.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(CPF.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    if (value instanceof String cpfRaw) {
                        Matcher matcher = CPF_PATTERN.matcher(cpfRaw);
                        if (!matcher.matches()) {
                            throw new IllegalArgumentException(
                                field.getAnnotation(CPF.class).message()
                            );
                        }



                        String formatted = String.format("%s.%s.%s-%s",
                            matcher.group(1), matcher.group(2),
                            matcher.group(3), matcher.group(4)
                        );


                        field.set(entity, formatted);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field: " + field.getName(), e);
                }
            }
        }
    }
}
