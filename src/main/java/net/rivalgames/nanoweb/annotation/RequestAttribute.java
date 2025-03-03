package net.rivalgames.nanoweb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @date 2/22/2025
 * @author Moose1301
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestAttribute {
    /**
     * @return The Context Attribute Name ({@link io.javalin.http.Context#attribute(String)})
     */
    String value();
}
