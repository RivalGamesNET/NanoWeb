package net.rivalgames.nanoweb.annotation;

/**
 * @date 3/3/2025
 * @author Moose1301
 */
public @interface RequestFormParam {
    /**
     * If the parameter type is {@link io.javalin.http.UploadedFile}
     * Tt will be retrieved using {@link io.javalin.http.Context#uploadedFile(String)}
     *
     * @return The Form Param Name ({@link io.javalin.http.Context#formParam(String)})
     */
    String value();
}
