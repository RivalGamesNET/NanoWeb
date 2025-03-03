package net.rivalgames.nanoweb.processor;

import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import lombok.Getter;
import net.rivalgames.nanoweb.NanoWeb;
import net.rivalgames.nanoweb.annotation.MethodMapping;
import net.rivalgames.nanoweb.annotation.RateLimit;
import net.rivalgames.nanoweb.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public class AnnotationProcessor {

    private final Map<HandlerType, Map<String, MethodInfo>> mappings = new HashMap<>();

    public void registerControllers(Object... controllers) {
        for (Object controller : controllers)
            this.registerController(controller);
    }

    private void registerController(Object controller) {
        Class<?> clazz = controller.getClass();
        if (!clazz.isAnnotationPresent(RestController.class))
            return;

        RestController restController = clazz.getAnnotation(RestController.class);
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodMapping.class)) {
                MethodMapping mapping = method.getAnnotation(MethodMapping.class);

                String pathPattern = restController.value() + "/" + mapping.value();
                Map<String, MethodInfo> methodMappings = mappings.getOrDefault(mapping.method(), new HashMap<>());
                methodMappings.put(pathPattern, new MethodInfo(controller, method));

                mappings.put(mapping.method(), methodMappings);
            }
        }
    }
}