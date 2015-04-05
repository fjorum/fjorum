package org.fjorum.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Gets.class)
public @interface Get {
    String value();
}
