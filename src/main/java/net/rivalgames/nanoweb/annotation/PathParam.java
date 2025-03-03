package net.rivalgames.nanoweb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PathParam {
    /**
     * @return The Path Name ({@link io.javalin.http.Context#pathParam(String)})
     */
    String value();
}
