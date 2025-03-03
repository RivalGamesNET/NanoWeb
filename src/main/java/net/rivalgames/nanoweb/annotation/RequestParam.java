package net.rivalgames.nanoweb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    /**
     * @return The request param to retrieve {@link io.javalin.http.Context#queryParam(String)}
     */
    String value();

    String defaultValue() default "";
}