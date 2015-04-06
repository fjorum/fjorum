package org.fjorum.controllers.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Gets.class)
public @interface Get {
    String value();
}
