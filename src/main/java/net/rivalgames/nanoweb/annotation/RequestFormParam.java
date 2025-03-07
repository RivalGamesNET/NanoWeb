package net.rivalgames.nanoweb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @date 3/3/2025
 * @author Moose1301
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestFormParam {
    /**
     * If the parameter type is {@link io.javalin.http.UploadedFile}
     * Tt will be retrieved using {@link io.javalin.http.Context#uploadedFile(String)}
     *
     * @return The Form Param Name ({@link io.javalin.http.Context#formParam(String)})
     */
    String value();
}
