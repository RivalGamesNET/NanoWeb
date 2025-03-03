package net.rivalgames.nanoweb.controller;

import com.google.gson.JsonObject;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import net.rivalgames.nanoweb.annotation.*;
import net.rivalgames.nanoweb.model.Response;

/**
 * @author Moose1301
 * @date 10/6/2024
 */
@RestController("testing")
public class ExampleController {

    @MethodMapping("working")
    public Response handleWorking() {
        return new Response("Hello World", HttpStatus.OK);
    }

    @MethodMapping(value = "update", method = HandlerType.POST)
    public Response handleWorkingPost(@RequestBody JsonObject body) {
        return new Response("Body: " + body.toString(), HttpStatus.OK);
    }

    @MethodMapping(value = "get/{test}/", method = HandlerType.GET)
    public Response handleQueryParam(@PathParam(value = "test") String body) {
        return new Response("Path: " + body, HttpStatus.OK);
    }

    @MethodMapping(value = "get/{test}/info", method = HandlerType.GET)
    public Response handleQueryParamDream(@PathParam(value = "test") String body) {
        return new Response("Info Path: " + body, HttpStatus.OK);
    }

    @MethodMapping(value = "get", method = HandlerType.GET)
    public Response asd(@RequestParam(value = "test", defaultValue = "default") String body) {
        return new Response("Query: " + body, HttpStatus.OK);
    }
}
