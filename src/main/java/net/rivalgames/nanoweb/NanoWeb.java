package net.rivalgames.nanoweb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.*;
import io.javalin.json.JavalinGson;
import lombok.Getter;
import lombok.Setter;
import net.rivalgames.nanoweb.annotation.*;
import net.rivalgames.nanoweb.authentication.AuthenticationHandler;
import net.rivalgames.nanoweb.model.Response;
import net.rivalgames.nanoweb.processor.AnnotationProcessor;
import net.rivalgames.nanoweb.processor.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * @author Moose1301
 * @date 10/6/2024
 */
public class NanoWeb {

    private final AnnotationProcessor processor;
    private final TreeSet<AuthenticationHandler> authenticationHandlers = new TreeSet<>();

    public static Gson GSON;

    @Getter
    private final Javalin javalin;

    @Setter
    private boolean debug = false;

    public NanoWeb(int port) {
        this(port, new GsonBuilder().create());
    }

    public NanoWeb(int port, Consumer<JavalinConfig> configConsumer) {
        this(port, new GsonBuilder().create(), configConsumer);
    }

    public NanoWeb(int port, Gson gson) {
        this(port, gson, (config) -> {
        });
    }

    public NanoWeb(int port, Gson gson, Consumer<JavalinConfig> configConsumer) {
        GSON = gson;

        this.processor = new AnnotationProcessor();

        this.javalin = Javalin.create(javalinConfig -> {
            javalinConfig.showJavalinBanner = false;
            javalinConfig.jsonMapper(new JavalinGson());
            configConsumer.accept(javalinConfig);
        }).start(port);
    }

    public void quietLoggers() {
        List<String> ignoreLogs = List.of(
                "io.javalin.Javalin",
                "org.eclipse.jetty.server.Server",
                "org.eclipse.jetty.server.session.DefaultSessionIdManager",
                "org.eclipse.jetty.server.handler.ContextHandler",
                "org.eclipse.jetty.server.AbstractConnector"
        );

        for (String s : ignoreLogs)
            System.setProperty("org.slf4j.simpleLogger.log." + s, "error");
    }


    public void shutdown() {
        javalin.stop();
    }

    public void register() {
        registerRoutes(javalin);

        javalin.exception(UnauthorizedResponse.class, (unauthorizedResponse, context) -> {
        });
        javalin.beforeMatched(ctx -> {
            if (authenticationHandlers.isEmpty())
                return;

            Boolean allowed = null;
            for (AuthenticationHandler handler : authenticationHandlers) {
                allowed = handler.handle(ctx);

                if (allowed == null)
                    continue;

                if (!allowed)
                    throw new UnauthorizedResponse();

                break;
            }

            if (allowed == null)
                throw new UnauthorizedResponse();
        });
    }

    public void registerAuthenticationHandlers(AuthenticationHandler... handlers) {
        authenticationHandlers.addAll(Arrays.asList(handlers));
    }

    public void registerAuthenticationHandler(AuthenticationHandler handler) {
        authenticationHandlers.add(handler);
    }

    public void registerRoutes(Javalin javalin) {
        for (Map.Entry<HandlerType, Map<String, MethodInfo>> entry : processor.getMappings().entrySet()) {
            for (Map.Entry<String, MethodInfo> typeRoutes : entry.getValue().entrySet()) {
                javalin.addHttpHandler(entry.getKey(), typeRoutes.getKey(), context -> {
                    Response response = serve(context, typeRoutes.getValue());

                    context.status(response.status());
                    if (response.json() && response.result() != null)
                        context.json(response.result());
                    else if (response.result() != null)
                        context.result(response.result());


                });
            }
        }
    }

    public Response serve(Context context, MethodInfo methodInfo) {
        if (methodInfo == null)
            return new Response("404 Not Found", HttpStatus.NOT_FOUND);

        if (debug)
            System.out.println("Method: " + methodInfo.method().toGenericString() + " Context: " + context.fullUrl());

        try {
            return invokeControllerMethod(methodInfo, context);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("500 Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Response invokeControllerMethod(MethodInfo methodInfo, Context context) throws Exception {
        Method method = methodInfo.method();
        Object controller = methodInfo.controller();

        Object[] args = new Object[method.getParameterCount()];
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Class<?>[] paramTypes = method.getParameterTypes();

        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation annotation : paramAnnotations[i]) {
                switch (annotation) {
                    case RequestParam param -> {
                        String queryParamValue = context.queryParam(param.value());
                        if (queryParamValue == null)
                            queryParamValue = param.defaultValue();

                        args[i] = toValue(queryParamValue, paramTypes[i]);
                    }

                    case PathParam param -> {
                        args[i] = toValue(context.pathParam(param.value()), paramTypes[i]);
                    }

                    case RequestBody ignored -> {
                        args[i] = toValue(context.body(), paramTypes[i]);
                    }

                    case RequestContext ignored -> args[i] = context;
                    case RequestAttribute requestAttribute -> args[i] = context.attribute(requestAttribute.value());
                    case RequestFormParam requestForm -> {
                        if (paramTypes[i] == UploadedFile.class)
                            args[i] = context.uploadedFile(requestForm.value());
                        else
                            args[i] = toValue(context.formParam(requestForm.value()), paramTypes[i]);
                    }
                    default -> args[i] = null;
                }
            }
        }

        return (Response) method.invoke(controller, args);
    }

    public void registerControllers(Object... objects) {
        processor.registerControllers(objects);
    }

    private Object toValue(String data, Class<?> type) {
        if (type == String.class)
            return data;

        return GSON.fromJson(data, type);
    }
}
