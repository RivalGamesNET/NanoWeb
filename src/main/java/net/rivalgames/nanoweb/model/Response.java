package net.rivalgames.nanoweb.model;

import com.google.gson.JsonElement;
import io.javalin.http.HttpStatus;
import net.rivalgames.nanoweb.NanoWeb;
import org.jetbrains.annotations.Nullable;

/**
 * @author Moose1301
 * @date 10/7/2024
 */

public record Response(HttpStatus status, @Nullable String result, boolean json) {

    public Response(JsonElement element, HttpStatus status) {
        this(status, NanoWeb.GSON.toJson(element), true);
    }

    public Response(String result, HttpStatus status) {
        this(status, result, false);
    }

    public Response(HttpStatus status) {
        this(status, null, false);
    }

}
