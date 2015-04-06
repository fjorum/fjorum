package org.fjorum.controllers.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Posts.class)
public @interface Post {
    String value();
}
