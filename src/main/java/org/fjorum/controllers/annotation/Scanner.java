package org.fjorum.controllers.annotation;

import ninja.Router;

import java.lang.reflect.Method;

public interface Scanner {

    default void scan(Router router, Class<?>... controllerClasses) {
        for (Class<?> controllerClass : controllerClasses) {
            for (Method method : controllerClass.getMethods()) {
                for (Get get : method.getAnnotationsByType(Get.class)) {
                    router.GET().route(get.value()).with(controllerClass, method.getName());
                }
                for (Post post : method.getAnnotationsByType(Post.class)) {
                    router.POST().route(post.value()).with(controllerClass, method.getName());
                }
            }
        }
    }
}
