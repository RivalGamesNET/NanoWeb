package net.rivalgames.nanoweb.annotation;


import io.javalin.http.HandlerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Moose1301
 * @date 10/6/2024
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodMapping {
    /**
     * @return Endpoint
     */
    String value();

    HandlerType method() default HandlerType.GET;
}
