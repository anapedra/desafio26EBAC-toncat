package org.anasantana.annotetions;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CPF {
    String message() default "Invalid CPF format";
}
